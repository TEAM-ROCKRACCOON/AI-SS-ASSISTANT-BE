package com.ai_ss.domain.user.api.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai_ss.domain.user.api.dto.response.AccessTokenGenerateResponse;
import com.ai_ss.domain.user.api.dto.response.LoginSuccessResponse;
import com.ai_ss.domain.user.core.entity.User;
import com.ai_ss.domain.user.core.entity.enums.Role;
import com.ai_ss.domain.user.core.repository.UserRepository;
import com.ai_ss.global.auth.client.dto.UserSocialInfoResponse;
import com.ai_ss.global.auth.jwt.exception.TokenErrorCode;
import com.ai_ss.global.auth.jwt.provider.JwtTokenProvider;
import com.ai_ss.global.auth.jwt.provider.JwtValidationType;
import com.ai_ss.global.auth.jwt.service.TokenService;
import com.ai_ss.global.auth.security.MemberAuthentication;
import com.ai_ss.global.common.exception.AiSsException;
import com.ai_ss.global.common.exception.code.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private static final String BEARER_PREFIX = "Bearer ";
	private final JwtTokenProvider jwtTokenProvider;
	private final TokenService tokenService;
	private final UserRepository userRepository;

	/**
	 * 사용자의 로그인 성공 시 Access Token과 Refresh Token을 생성하고,
	 * 로그인 성공 응답 객체(LoginSuccessResponse)를 반환하는 메서드.
	 *
	 * @param userId 회원의 고유 ID
	 * @param userSocialInfoResponse 로그인 시 외부로부터 전달된 회원 정보
	 * @return 로그인 성공 응답(LoginSuccessResponse)
	 */
	public LoginSuccessResponse generateLoginSuccessResponse(final Long userId,
		final UserSocialInfoResponse userSocialInfoResponse) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AiSsException(ErrorCode.USER_NOT_FOUND));		final Role role = user.getRole();
		final String nickname = user.getNickname();

		Collection<GrantedAuthority> authorities = List.of(role.toGrantedAuthority());

		log.info("Starting login success response generation for userId: {}, nickname: {}, role: {}", userId,
			nickname,
			role.getRoleName());

		UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(userId, role,
			authorities);
		String refreshToken = issueAndSaveRefreshToken(userId, authenticationToken);
		String accessToken = jwtTokenProvider.issueAccessToken(authenticationToken);

		log.info("Login success for authorities: {}, accessToken: {}, refreshToken: {}", authorities, accessToken,
			refreshToken);

		return LoginSuccessResponse.of(accessToken, refreshToken, nickname, role);
	}

	/**
	 * 쿠키에서 "refreshToken" 값을 가져와 유효성을 검증하고,
	 * 유효한 Refresh Token일 경우 새로운 Access Token을 생성합니다.
	 *
	 * Refresh Token에서 사용자 ID와 Role 정보를 추출한 후,
	 * Role에 따라 Admin 또는 store 권한으로 새로운 Access Token을 발급합니다.
	 *
	 * @param refreshToken "사용자의 Refresh Token"
	 * @return 새로운 Access Token 정보가 포함된 AccessTokenGenerateResponse 객체
	 */
	@Transactional
	public AccessTokenGenerateResponse generateAccessTokenFromRefreshToken(final String refreshToken) {
		validateRefreshToken(refreshToken);

		Long userId = jwtTokenProvider.getUserIdFromJwt(refreshToken);
		verifyUserIdWithStoredToken(refreshToken, userId);

		Role role = jwtTokenProvider.getRoleFromJwt(refreshToken);
		Collection<GrantedAuthority> authorities = List.of(role.toGrantedAuthority());

		UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(userId, role, authorities);
		log.info("Generated new access token for userId: {}, role: {}, authorities: {}",
			userId, role.getRoleName(), authorities);

		return AccessTokenGenerateResponse.from(jwtTokenProvider.issueAccessToken(authenticationToken));
	}

	@Transactional
	public String generateRefreshTokenFromOldRefreshToken(String oldRefreshToken, Role role) {
		validateRefreshToken(oldRefreshToken);

		Long userId = jwtTokenProvider.getUserIdFromJwt(oldRefreshToken);
		verifyUserIdWithStoredToken(oldRefreshToken, userId);

		Collection<GrantedAuthority> authorities = List.of(role.toGrantedAuthority());

		UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(userId, role, authorities);
		log.info("Generated new refresh token for userId: {}, role: {}, authorities: {}",
			userId, role.getRoleName(), authorities);

		return jwtTokenProvider.issueRefreshToken(authenticationToken);
	}

	/**
	 * Refresh Token을 발급하고 저장하는 메서드.
	 * 발급된 Refresh Token을 TokenService에 저장
	 *
	 * @param userId 회원의 고유 ID
	 * @param authenticationToken 사용자 인증 정보
	 * @return 발급된 Refresh Token
	 */
	private String issueAndSaveRefreshToken(Long userId, UsernamePasswordAuthenticationToken authenticationToken) {
		String refreshToken = jwtTokenProvider.issueRefreshToken(authenticationToken);
		log.info("Issued new refresh token for userId: {}", userId);
		tokenService.saveRefreshToken(userId, refreshToken);
		return refreshToken;
	}

	/**
	 * 사용자 Role에 따라 적절한 Authentication 객체(Admin 또는 Store)를 생성하는 메서드.
	 *
	 * @param userId 회원의 고유 ID
	 * @param role 사용자 Role (ADMIN 또는 Store)
	 * @param authorities 사용자에게 부여된 권한 목록
	 * @return 생성된 Admin 또는 Store Authentication 객체
	 */
	private UsernamePasswordAuthenticationToken createAuthenticationToken(Long userId, Role role,
		Collection<GrantedAuthority> authorities) {
		log.info("Creating MemberAuthentication for userId: {}", userId);
			return new MemberAuthentication(userId, null, authorities);

	}

	private void validateRefreshToken(String refreshToken) {
		JwtValidationType validationType = jwtTokenProvider.validateToken(refreshToken);

		if (!validationType.equals(JwtValidationType.VALID_JWT)) {
			throw switch (validationType) {
				case EXPIRED_JWT_TOKEN -> new AiSsException(TokenErrorCode.REFRESH_TOKEN_EXPIRED_ERROR);
				case INVALID_JWT_TOKEN -> new AiSsException(TokenErrorCode.INVALID_REFRESH_TOKEN_ERROR);
				case INVALID_JWT_SIGNATURE -> new AiSsException(TokenErrorCode.REFRESH_TOKEN_SIGNATURE_ERROR);
				case UNSUPPORTED_JWT_TOKEN -> new AiSsException(TokenErrorCode.UNSUPPORTED_REFRESH_TOKEN_ERROR);
				case EMPTY_JWT -> new AiSsException(TokenErrorCode.REFRESH_TOKEN_EMPTY_ERROR);
				default -> new AiSsException(TokenErrorCode.UNKNOWN_REFRESH_TOKEN_ERROR);
			};
		}
	}

	private void verifyUserIdWithStoredToken(String refreshToken, Long userId) {
		Long storedUserId = tokenService.findIdByRefreshToken(refreshToken);

		if (!userId.equals(storedUserId)) {
			log.error("UserId mismatch: token does not match the stored refresh token");
			throw new AiSsException(TokenErrorCode.REFRESH_TOKEN_STORE_ID_MISMATCH_ERROR);
		}
	}
}

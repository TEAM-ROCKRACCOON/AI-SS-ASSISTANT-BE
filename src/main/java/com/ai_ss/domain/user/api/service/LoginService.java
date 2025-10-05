package com.ai_ss.domain.user.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai_ss.domain.user.api.dto.response.LoginSuccessResponse;
import com.ai_ss.domain.user.core.entity.User;
import com.ai_ss.domain.user.core.entity.enums.SocialType;
import com.ai_ss.global.auth.client.dto.UserSocialInfoResponse;
import com.ai_ss.global.auth.client.dto.StoreSocialLoginRequest;
import com.ai_ss.global.auth.client.service.GoogleSocialService;
import com.ai_ss.global.auth.client.service.SocialService;
import com.ai_ss.global.common.exception.AiSsException;
import com.ai_ss.global.common.exception.code.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

	private final AuthenticationService authenticationService;
	private final GoogleSocialService googleSocialService;
	private final UserService userService;

	@Transactional
	public LoginSuccessResponse login(String authorizationCode, StoreSocialLoginRequest request) {
		try {
			UserSocialInfoResponse userInfo = findUserInfo(authorizationCode, request);
			Long userId = findOrRegisterUser(userInfo);
			return returnLoginSuccessResponse(userId, userInfo);
		} catch (Exception e) {
			log.error("Login failed: ", e);
			throw e;
		}
	}

	private UserSocialInfoResponse findUserInfo(
		String authorizationCode,
		StoreSocialLoginRequest request
	) {
		SocialService socialService = getSocialService(request.socialType());
		return socialService.login(authorizationCode, request);
	}

	private SocialService getSocialService(SocialType socialType) {
		return switch(socialType) {
			case GOOGLE -> googleSocialService;
			default -> throw new AiSsException(ErrorCode.SOCIAL_TYPE_NOT_SUPPORTED);
		};
	}

	private Long findOrRegisterUser(final UserSocialInfoResponse userSocialInfoResponse) {
		final String socialId = userSocialInfoResponse.socialId();
		final SocialType socialType = userSocialInfoResponse.socialType();

		User user = userService.findUserBySocialIdAndSocialType(socialId, socialType);
		if (user != null) {
			return user.getId();
		}

		return userService.registerUser(userSocialInfoResponse);
	}

	private LoginSuccessResponse returnLoginSuccessResponse(
		Long userId, UserSocialInfoResponse userSocialInfoResponse) {

		return authenticationService.generateLoginSuccessResponse(userId, userSocialInfoResponse);
	}
}

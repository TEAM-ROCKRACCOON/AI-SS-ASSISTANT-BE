package com.ai_ss.domain.user.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai_ss.domain.user.api.dto.request.AddressRequest;
import com.ai_ss.domain.user.api.dto.request.HabitRequest;
import com.ai_ss.domain.user.api.dto.request.NicknameRequest;
import com.ai_ss.domain.user.api.dto.request.ProfileUpdateRequest;
import com.ai_ss.domain.user.api.dto.response.AccessTokenGenerateResponse;
import com.ai_ss.domain.user.api.dto.response.LoginSuccessResponse;
import com.ai_ss.domain.user.api.dto.response.ProfileResponse;
import com.ai_ss.domain.user.api.exception.UserSucessCode;
import com.ai_ss.domain.user.api.service.AuthenticationService;
import com.ai_ss.domain.user.api.service.LoginService;
import com.ai_ss.domain.user.api.service.UserService;
import com.ai_ss.global.auth.annotation.CurrentMember;
import com.ai_ss.global.auth.client.dto.StoreSocialLoginRequest;
import com.ai_ss.global.auth.jwt.service.TokenService;
import com.ai_ss.global.common.exception.dto.SuccessResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private static final String REFRESH_TOKEN = "refreshToken";
	private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;
	private final LoginService loginService;
	private final TokenService tokenService;
	private final UserService userService;
	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<SuccessResponse<LoginSuccessResponse>> login(
		@RequestParam("authorizationCode") String authorizationCode,
		@RequestBody StoreSocialLoginRequest storeSocialLoginRequest,
		HttpServletResponse httpServletResponse
	) {
		LoginSuccessResponse successResponse = loginService.login(authorizationCode, storeSocialLoginRequest);
		ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, successResponse.refreshToken())
			.maxAge(COOKIE_MAX_AGE)
			.path("/")
			.secure(true)
			.sameSite("None")
			.httpOnly(true)
			.build();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(SuccessResponse.of(UserSucessCode.LOGIN_SUCCESS, successResponse));
	}

	@PostMapping("/logout")
	public ResponseEntity<SuccessResponse<Void>> logOut(
		@CurrentMember final Long userId
	) {
		tokenService.deleteRefreshToken(userId);
		return ResponseEntity.ok().body(SuccessResponse.of(UserSucessCode.LOGOUT_SUCCESS));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<SuccessResponse<AccessTokenGenerateResponse>> reissue(
		@CookieValue("refreshToken") String refreshToken
	) {
		AccessTokenGenerateResponse accessTokenGenerateResponse = authenticationService.generateAccessTokenFromRefreshToken(refreshToken);
		return ResponseEntity.ok(SuccessResponse.of(UserSucessCode.ACCESS_TOKEN_REISSUE_SUCCESS, accessTokenGenerateResponse));
	}

	@PostMapping("/nickname/register")
	public ResponseEntity<SuccessResponse<Void>> registerNickname(
		@CurrentMember final Long userId,
		@RequestBody final NicknameRequest request
	) {
		userService.registerNickname(userId, request.nickname());
		return ResponseEntity.ok(SuccessResponse.of(UserSucessCode.NICKNAME_REGISTER_SUCCESS));
	}

	@PostMapping("/address")
	public ResponseEntity<SuccessResponse<Void>> registerAddress(
		@CurrentMember final Long userId,
		@RequestBody final AddressRequest request
	) {
		userService.registerAddress(userId, request);
		return ResponseEntity.ok(SuccessResponse.of(UserSucessCode.ADDRESS_REGISTER_SUCCESS));
	}

	@PostMapping("/habit")
	public ResponseEntity<SuccessResponse<Void>> registerHabit(
		@CurrentMember final Long userId,
		@RequestBody final HabitRequest request
	) {
		userService.registerHabit(userId, request);
		return ResponseEntity.ok(SuccessResponse.of(UserSucessCode.HABIT_REGISTER_SUCCESS));
	}

	@GetMapping("/profile")
	public ResponseEntity<SuccessResponse<ProfileResponse>> getProfile(
		@CurrentMember final Long userId
	) {
		return ResponseEntity.ok(SuccessResponse.of(
			UserSucessCode.PROFILE_RETRIEVE_SUCCESS, userService.getProfile(userId)
		));
	}

	@PutMapping("/profile")
	public ResponseEntity<SuccessResponse<Void>> updateProfile(
		@CurrentMember final Long userId,
		@RequestBody final ProfileUpdateRequest request
	) {
		userService.updateProfile(userId, request);
		return ResponseEntity.ok(SuccessResponse.of(UserSucessCode.PROFILE_UPDATE_SUCCESS));
	}

	@DeleteMapping("/withdraw")
	public ResponseEntity<SuccessResponse<Void>> withdraw(
		@CurrentMember final Long userId
	) {
		userService.withdraw(userId);
		return ResponseEntity.ok(SuccessResponse.of(UserSucessCode.WITHDRAW_SUCCESS));
	}
}

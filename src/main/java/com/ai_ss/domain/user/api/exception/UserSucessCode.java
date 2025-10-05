package com.ai_ss.domain.user.api.exception;

import org.springframework.http.HttpStatus;

import com.ai_ss.global.common.exception.base.BaseSuccessCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSucessCode implements BaseSuccessCode {
	/*
	200 Ok
	 */
	LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
	ACCESS_TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "액세스 토큰 재발급 성공"),
	NICKNAME_REGISTER_SUCCESS(HttpStatus.OK, "닉네임이 등록되었습니다."),
	ADDRESS_REGISTER_SUCCESS(HttpStatus.OK, "주소가 등록되었습니다."),
	HABIT_REGISTER_SUCCESS(HttpStatus.OK, "청소습관 정보가 등록되었습니다."),
	PROFILE_RETRIEVE_SUCCESS(HttpStatus.OK, "프로필 정보 조회에 성공했습니다."),
	PROFILE_UPDATE_SUCCESS(HttpStatus.OK, "프로필 정보 수정에 성공했습니다."),
	WITHDRAW_SUCCESS(HttpStatus.OK, "회원 탈퇴에 성공했습니다."),

	/*
	201 Created
	*/
	LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
	;

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
}

package com.ai_ss.global.auth.client.exception;

import org.springframework.http.HttpStatus;

import com.ai_ss.global.common.exception.base.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthErrorCode implements BaseErrorCode {

	/*
	401 UNAUTHORIZED
 	*/
	O_AUTH_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "OAuth 인증에 접근할 수 없습니다."),
	GET_INFO_ERROR(HttpStatus.UNAUTHORIZED, "사용자의 정보를 가져올 수 없습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}

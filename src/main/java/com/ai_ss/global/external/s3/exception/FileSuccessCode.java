package com.ai_ss.global.external.s3.exception;

import org.springframework.http.HttpStatus;

import com.ai_ss.global.common.exception.base.BaseSuccessCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileSuccessCode implements BaseSuccessCode {
	PRESIGNED_URL_ISSUED(HttpStatus.OK, "Presigned URL 발급 성공");

	private final HttpStatus httpStatus;
	private final String message;
}

package com.ai_ss.global.common.exception.base;

import org.springframework.http.HttpStatus;

public interface BaseSuccessCode {
	HttpStatus getHttpStatus();

	String getMessage();
}

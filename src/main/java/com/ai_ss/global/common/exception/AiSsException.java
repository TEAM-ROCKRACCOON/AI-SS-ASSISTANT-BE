package com.ai_ss.global.common.exception;

import com.ai_ss.global.common.exception.base.BaseErrorCode;

import lombok.Getter;

@Getter
public class AiSsException extends RuntimeException {
	private final BaseErrorCode baseErrorCode;

	public AiSsException(BaseErrorCode baseErrorCode) {
		super(baseErrorCode.getMessage());
		this.baseErrorCode = baseErrorCode;
	}
}

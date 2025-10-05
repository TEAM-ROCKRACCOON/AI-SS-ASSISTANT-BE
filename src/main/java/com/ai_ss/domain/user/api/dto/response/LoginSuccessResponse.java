package com.ai_ss.domain.user.api.dto.response;

import com.ai_ss.domain.user.core.entity.enums.Role;

public record LoginSuccessResponse(
	String accessToken,
	String refreshToken,
	String nickname,
	Role role
) {
	public static LoginSuccessResponse of(
		final String accessToken,
		final String refreshToken,
		final String nickname,
		final Role role
	) {
		return new LoginSuccessResponse(accessToken, refreshToken, nickname, role);
	}
}
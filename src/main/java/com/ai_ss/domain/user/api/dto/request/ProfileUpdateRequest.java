package com.ai_ss.domain.user.api.dto.request;

public record ProfileUpdateRequest(
	String nickname,
	String email
) {
}

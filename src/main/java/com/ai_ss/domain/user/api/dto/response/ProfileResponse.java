package com.ai_ss.domain.user.api.dto.response;

public record ProfileResponse(
	String nickname,
	String email,
	String termsUrl
) {
	public static ProfileResponse of(String nickname, String email, String termsUrl) {
		return new ProfileResponse(nickname, email, termsUrl);
	}
}

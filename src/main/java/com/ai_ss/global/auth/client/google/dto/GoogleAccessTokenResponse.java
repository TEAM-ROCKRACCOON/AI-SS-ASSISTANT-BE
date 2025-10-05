package com.ai_ss.global.auth.client.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleAccessTokenResponse(
	@JsonProperty("access_token") String accessToken,
	@JsonProperty("expires_in") Integer expiresIn,
	@JsonProperty("token_type") String tokenType,
	@JsonProperty("refresh_token") String refreshToken,
	@JsonProperty("id_token") String idToken,
	@JsonProperty("scope") String scope
) {
	// public static GoogleAccessTokenResponse from(
	// 	final String accessToken
	// ) {
	// 	return new GoogleAccessTokenResponse(
	// 		accessToken
	// 	);
	// }
}

package com.ai_ss.global.auth.client.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ai_ss.global.auth.client.google.dto.GoogleAccessTokenResponse;

@FeignClient(
	name = "google-auth-client",
	url = "https://oauth2.googleapis.com"
)
public interface GoogleAuthApiClient {

	@PostMapping(
		value = "/token",
		consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	GoogleAccessTokenResponse getOAuth2AccessToken(@RequestBody MultiValueMap<String, String> form);

	// @PostMapping("/token")
	// GoogleAccessTokenResponse getOAuth2AccessToken(
	// 	@RequestParam("grant_type") String grantType,
	// 	@RequestParam("client_id") String clientId,
	// 	@RequestParam("client_secret") String clientSecret,
	// 	@RequestParam("redirect_uri") String redirectUri,
	// 	@RequestParam("code") String authorizationCode
	// );
}

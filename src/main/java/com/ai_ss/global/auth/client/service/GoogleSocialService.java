package com.ai_ss.global.auth.client.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.ai_ss.domain.user.core.entity.enums.SocialType;
import com.ai_ss.global.auth.client.dto.UserSocialInfoResponse;
import com.ai_ss.global.auth.client.dto.StoreSocialLoginRequest;
import com.ai_ss.global.auth.client.exception.OAuthErrorCode;
import com.ai_ss.global.auth.client.google.GoogleApiClient;
import com.ai_ss.global.auth.client.google.GoogleAuthApiClient;
import com.ai_ss.global.auth.client.google.dto.GoogleAccessTokenResponse;
import com.ai_ss.global.auth.client.google.dto.GoogleUserResponse;
import com.ai_ss.global.common.exception.AiSsException;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleSocialService implements SocialService {

	private static final String AUTH_CODE = "authorization_code";

	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String redirectUri;

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String clientSecret;

	private final GoogleApiClient googleApiClient;
	private final GoogleAuthApiClient googleAuthApiClient;

	@Override
	public UserSocialInfoResponse login(
		final String authorizationCode,
		final StoreSocialLoginRequest loginRequest
	) {
		String accessToken;
		try {
			accessToken = getOAuth2Authentication(authorizationCode);
		} catch (FeignException e) {
			throw new AiSsException(OAuthErrorCode.O_AUTH_TOKEN_ERROR);
		}

		return getLoginDto(loginRequest.socialType(), getUserInfo(accessToken));
	}

	private String getOAuth2Authentication(final String authorizationCode) {
		try {
			GoogleAccessTokenResponse res = WebClient.builder()
				.baseUrl("https://oauth2.googleapis.com")
				.build()
				.post()
				.uri("/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("grant_type", "authorization_code")
					.with("client_id", clientId)
					.with("client_secret", clientSecret)
					.with("redirect_uri", redirectUri)
					.with("code", authorizationCode))
				.retrieve()
				.bodyToMono(GoogleAccessTokenResponse.class)
				.block();

			if (res == null || res.accessToken() == null) {
				throw new AiSsException(OAuthErrorCode.O_AUTH_TOKEN_ERROR);
			}
			return "Bearer " + res.accessToken();
		} catch (Exception e) {
			log.error("Google token exchange failed", e);
			throw new AiSsException(OAuthErrorCode.O_AUTH_TOKEN_ERROR);
		}
	}

	// private String getOAuth2Authentication(
	// 	final String authorizationCode) {
	// 	GoogleAccessTokenResponse response;
	// 	try {
	// 		response = googleAuthApiClient.getOAuth2AccessToken(
	// 			AUTH_CODE,
	// 			clientId,
	// 			clientSecret,
	// 			redirectUri,
	// 			authorizationCode
	// 		);
	// 	} catch (FeignException e) {
	// 		throw new AiSsException(OAuthErrorCode.O_AUTH_TOKEN_ERROR);
	// 	}
	// 	return "Bearer " + response.accessToken();
	// }

	private GoogleUserResponse getUserInfo(
		final String accessToken
	) {
		log.info("Fetching user info from Google API using access token");

		GoogleUserResponse response;
		try {
			response = googleApiClient.getUserInformation(accessToken);
			log.info("Successfully retrieved user info: ID = {}", response.sub());
		} catch (FeignException e) {
			log.error("Failed to retrieve user info from Google API. Error: {}", e.contentUTF8(), e);
			throw new AiSsException(OAuthErrorCode.GET_INFO_ERROR);
		}
		return response;
	}

	private UserSocialInfoResponse getLoginDto(
		final SocialType socialType,
		final GoogleUserResponse googleUserResponse
	) {
		return UserSocialInfoResponse.of(
			googleUserResponse.sub(),
			socialType,
			googleUserResponse.email(),
			googleUserResponse.givenName()
		);
	}
}

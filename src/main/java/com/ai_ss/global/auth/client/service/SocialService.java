package com.ai_ss.global.auth.client.service;

import com.ai_ss.global.auth.client.dto.UserSocialInfoResponse;
import com.ai_ss.global.auth.client.dto.StoreSocialLoginRequest;

public interface SocialService {
	UserSocialInfoResponse login(
		final String authorizationToken,
		final StoreSocialLoginRequest storeSocialLoginRequest);
}

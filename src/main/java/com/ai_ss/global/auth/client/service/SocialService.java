package com.ai_ss.global.auth.client.service;

import com.ai_ss.global.auth.client.dto.StoreSocialInfoResponse;
import com.ai_ss.global.auth.client.dto.StoreSocialLoginRequest;

public interface SocialService {
	StoreSocialInfoResponse login(
		final String authorizationToken,
		final StoreSocialLoginRequest storeSocialLoginRequest);
}

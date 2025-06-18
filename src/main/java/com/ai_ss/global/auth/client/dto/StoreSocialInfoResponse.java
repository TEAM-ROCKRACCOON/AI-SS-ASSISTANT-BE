package com.ai_ss.global.auth.client.dto;

import com.ai_ss.domain.user.core.entity.enums.SocialType;

public record StoreSocialInfoResponse(
        String socialId,
        SocialType socialType
) {
        public static StoreSocialInfoResponse of(
                final String socialId,
                final SocialType socialType
        ){
            return new StoreSocialInfoResponse(socialId, socialType);
        }
}

package com.ai_ss.global.auth.client.dto;

import com.ai_ss.domain.user.core.entity.enums.SocialType;

public record UserSocialInfoResponse(
        String socialId,
        SocialType socialType,
        String email,
        String name
) {
        public static UserSocialInfoResponse of(
                final String socialId,
                final SocialType socialType,
            final String email,
            final String name
        ){
            return new UserSocialInfoResponse(socialId, socialType, email, name);
        }
}

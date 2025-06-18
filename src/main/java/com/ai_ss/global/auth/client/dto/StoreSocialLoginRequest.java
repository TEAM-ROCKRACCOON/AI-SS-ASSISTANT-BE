package com.ai_ss.global.auth.client.dto;


import com.ai_ss.domain.user.core.entity.enums.SocialType;

import jakarta.validation.constraints.NotNull;

public record StoreSocialLoginRequest(
        @NotNull(message = "소셜 로그인 종류가 입력되지 않았습니다.")
        SocialType socialType
) {
}

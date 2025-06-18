package com.ai_ss.domain.user.core.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserVo {
	private Long id;
	private String oauthId;
	private String nickname;
	private String email;
	private String profileImageUrl;
	private boolean onboardingCompleted;
}

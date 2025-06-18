package com.ai_ss.domain.feedback.core.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackVo {
	private Long userId;
	private int volumeScore;
	private int timeScore;
	private String text;
	private LocalDateTime submittedAt;
}
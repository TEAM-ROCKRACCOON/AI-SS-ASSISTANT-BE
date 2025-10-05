package com.ai_ss.domain.feedback.api.dto.request;

import jakarta.annotation.Nullable;

public record FeedbackRequest(
	String weekStartDate,
	int cleaningAmountScore,
	int recommendedTimeScore,
	@Nullable String comment
) {
}

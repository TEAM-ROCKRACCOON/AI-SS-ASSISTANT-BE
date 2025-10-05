package com.ai_ss.domain.feedback.api.dto.response;

import java.util.Map;

public record WeeklyCountsResponse(
	Map<String, Integer> counts
) {
}

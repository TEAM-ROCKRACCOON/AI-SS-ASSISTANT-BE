package com.ai_ss.domain.user.api.dto.request;

import java.util.List;

import com.ai_ss.domain.user.core.entity.enums.PreferredDay;

public record HabitRequest(
	String cleaningFrequency,
	String cleaningDistribution,
	String preferredTimeRange,
	List<PreferredDay> preferredDays,
	String itemQuantity,
	int sleepAt,
	int wakeAt
) {
}

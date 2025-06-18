package com.ai_ss.domain.user.core.entity.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CleaningPreference {
	private String preferredTime; // 예: "morning", "evening"
	private String style;         // 예: "간단하게", "디테일하게"
	private int frequency;        // 주간 청소 빈도
}
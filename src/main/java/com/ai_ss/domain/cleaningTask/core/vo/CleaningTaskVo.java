package com.ai_ss.domain.cleaningTask.core.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CleaningTaskVo {
	private Long id;
	private String name;
	private int estimatedMinutes;
}
package com.ai_ss.domain.routine.core.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoutineVo {
	private Long userId;
	private String title;
	private LocalDateTime startTime;
	private Integer durationMinutes; // 루틴 소요 시간 (분 단위)
	private Boolean completed;
}
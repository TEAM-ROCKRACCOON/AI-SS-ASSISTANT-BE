package com.ai_ss.domain.user.core.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CalendarScheduleVo {
	private String eventId;
	private String title;
	private LocalDateTime start;
	private LocalDateTime end;
	private String location;
}

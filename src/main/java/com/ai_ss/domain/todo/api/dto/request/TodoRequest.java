package com.ai_ss.domain.todo.api.dto.request;

public record TodoRequest(
	String title,
	String time,
	String date
) {
}

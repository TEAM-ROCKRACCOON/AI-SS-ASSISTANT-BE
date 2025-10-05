package com.ai_ss.domain.todo.api.dto.response;

import java.time.format.DateTimeFormatter;

import com.ai_ss.domain.todo.core.entity.Todo;

public record TodoDto(
	Long id,
	String title,
	String time,
	boolean isDone
) {
	public static TodoDto from(Todo todo) {
		String time = todo.getStartDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
		return new TodoDto(todo.getId(), todo.getTitle(), time, todo.getIsDone());
	}
}

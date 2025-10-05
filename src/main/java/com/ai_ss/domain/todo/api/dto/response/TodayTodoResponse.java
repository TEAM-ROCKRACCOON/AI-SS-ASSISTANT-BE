package com.ai_ss.domain.todo.api.dto.response;

import java.util.List;

import com.ai_ss.domain.todo.core.entity.Todo;

public record TodayTodoResponse(
	List<TodoDto> todos
) {
	public static TodayTodoResponse from(List<Todo> todos) {
		List<TodoDto> todoDtos = todos.stream()
			.map(TodoDto::from)
			.toList();

		return new TodayTodoResponse(todoDtos);
	}
}

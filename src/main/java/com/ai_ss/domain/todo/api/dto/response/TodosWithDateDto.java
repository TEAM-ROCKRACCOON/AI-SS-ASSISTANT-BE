package com.ai_ss.domain.todo.api.dto.response;

import java.util.List;

import com.ai_ss.domain.todo.core.entity.Todo;

public record TodosWithDateDto(
	String date,
	List<TodoDto> todos
) {
	public static TodosWithDateDto of(String date, List<Todo> todos) {
		List<TodoDto> todoDtos = todos.stream()
			.map(TodoDto::from)
			.toList();

		return new TodosWithDateDto(date, todoDtos);
	}
}

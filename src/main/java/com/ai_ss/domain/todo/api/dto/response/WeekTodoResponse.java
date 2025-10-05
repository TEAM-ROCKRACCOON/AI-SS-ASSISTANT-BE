package com.ai_ss.domain.todo.api.dto.response;

import static java.util.stream.Collectors.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ai_ss.domain.todo.core.entity.Todo;
import com.ai_ss.global.common.util.DateTimeUtil;

public record WeekTodoResponse(
	List<TodosWithDateDto> weeklyTodos
) {
	// startDate(yyyy-MM-dd) 포함 7일
	public static WeekTodoResponse ofWeek(List<Todo> todos, String startDate) {
		LocalDate start = DateTimeUtil.parseIsoDate(startDate);

		Map<String, List<Todo>> byDate = todos.stream()
			.collect(Collectors.groupingBy(t -> DateTimeUtil.formatDate(t.getStartDateTime())));

		List<TodosWithDateDto> list = new ArrayList<>(7);
		for (int i = 0; i < 7; i++) {
			LocalDate day = start.plusDays(i);
			String key = DateTimeUtil.formatDate(day);
			List<Todo> dayTodos = byDate.getOrDefault(key, List.of());
			list.add(TodosWithDateDto.of(key, dayTodos));
		}

		return new WeekTodoResponse(list);
	}
}

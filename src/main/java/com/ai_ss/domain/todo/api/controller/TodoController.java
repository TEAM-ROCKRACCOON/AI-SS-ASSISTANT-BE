package com.ai_ss.domain.todo.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai_ss.domain.todo.api.dto.request.TodoIsDoneUpdateRequest;
import com.ai_ss.domain.todo.api.dto.request.TodoRequest;
import com.ai_ss.domain.todo.api.dto.request.TodoTimeUpdateRequest;
import com.ai_ss.domain.todo.api.dto.response.TodayTodoResponse;
import com.ai_ss.domain.todo.api.dto.response.WeekTodoResponse;
import com.ai_ss.domain.todo.api.service.TodoService;
import com.ai_ss.global.auth.annotation.CurrentMember;
import com.ai_ss.global.common.exception.code.SuccessCode;
import com.ai_ss.global.common.exception.dto.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/todo")
@RequiredArgsConstructor
public class TodoController {

	private final TodoService todoService;

	@GetMapping("/today")
	public ResponseEntity<SuccessResponse<TodayTodoResponse>> getTodayTodo(
		@CurrentMember final Long userId
	) {
		return ResponseEntity.ok(SuccessResponse.of(
			SuccessCode.SUCCESS_FETCH, todoService.findTodayTodo(userId)
		));
	}

	@GetMapping("/week")
	public ResponseEntity<SuccessResponse<WeekTodoResponse>> getWeekTodo(
		@CurrentMember final Long userId,
		@RequestParam final String startDate
	) {
		return ResponseEntity.ok(SuccessResponse.of(
			SuccessCode.SUCCESS_FETCH, todoService.findWeekTodo(userId, startDate)
		));
	}

	@PostMapping
	public ResponseEntity<SuccessResponse<Void>> createTodo(
		@CurrentMember final Long userId,
		@RequestBody final TodoRequest request
	) {
		todoService.registerTodo(userId, request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@PatchMapping("/time/{todoId}")
	public ResponseEntity<SuccessResponse<Void>> updateTodoTime(
		@CurrentMember final Long userId,
		@PathVariable final Long todoId,
		@RequestBody final TodoTimeUpdateRequest request
	) {
		todoService.updateTodoTime(userId, todoId, request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
	}

	@PatchMapping("/isdone/{todoId}")
	public ResponseEntity<SuccessResponse<Void>> updateIsDone(
		@CurrentMember final Long userId,
		@PathVariable final Long todoId,
		@RequestBody final TodoIsDoneUpdateRequest request
	) {
		todoService.completeTodo(userId, todoId, request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_UPDATE));
	}

	@DeleteMapping("/{todoId}")
	public ResponseEntity<SuccessResponse<Void>> deleteTodo(
		@CurrentMember final Long userId,
		@PathVariable final Long todoId
	) {
		todoService.deleteTodo(userId, todoId);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_DELETE));
	}
}

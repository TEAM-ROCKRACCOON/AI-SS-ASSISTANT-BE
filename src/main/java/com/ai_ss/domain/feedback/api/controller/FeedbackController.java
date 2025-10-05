package com.ai_ss.domain.feedback.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai_ss.domain.feedback.api.dto.request.FeedbackRequest;
import com.ai_ss.domain.feedback.api.dto.response.WeeklyCountsResponse;
import com.ai_ss.domain.feedback.api.service.FeedbackService;
import com.ai_ss.domain.todo.api.service.TodoService;
import com.ai_ss.global.auth.annotation.CurrentMember;
import com.ai_ss.global.common.exception.code.SuccessCode;
import com.ai_ss.global.common.exception.dto.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users/feedback")
@RequiredArgsConstructor
public class FeedbackController {

	private final FeedbackService feedbackService;
	private final TodoService todoService;

	@PostMapping
	public ResponseEntity<SuccessResponse<Void>> registerFeedback(
		@CurrentMember final Long userId,
		@RequestBody FeedbackRequest request
	) {
		feedbackService.registerFeedback(userId, request);
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CREATE));
	}

	@GetMapping
	public ResponseEntity<SuccessResponse<WeeklyCountsResponse>> getWeeklyCounts(
		@CurrentMember final Long userId,
		@RequestParam final String weekStartDate
	) {
		return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_FETCH,
			todoService.getWeeklyCounts(userId, weekStartDate)));
	}
}

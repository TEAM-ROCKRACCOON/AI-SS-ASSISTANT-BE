package com.ai_ss.domain.feedback.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai_ss.domain.feedback.api.dto.request.FeedbackRequest;
import com.ai_ss.domain.feedback.core.entity.Feedback;
import com.ai_ss.domain.feedback.core.repository.FeedbackRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

	private final FeedbackRepository feedbackRepository;

	@Transactional
	public void registerFeedback(final Long userId, final FeedbackRequest feedbackRequest) {
		LocalDate weekStartDate = LocalDate.parse(feedbackRequest.weekStartDate(), DateTimeFormatter.ISO_DATE);
		Feedback feedback = Feedback.create(
			userId, weekStartDate, feedbackRequest.cleaningAmountScore(),
			feedbackRequest.recommendedTimeScore(), feedbackRequest.comment());
		feedbackRepository.save(feedback);
	}
}

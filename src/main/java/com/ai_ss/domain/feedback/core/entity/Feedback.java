package com.ai_ss.domain.feedback.core.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feedback")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private LocalDate weekStartDate;

	private int cleaningAmountScore; // 분량 만족도 (1~5)

	private int recommendedTimeScore;   // 시간 만족도 (1~5)

	private String comment;     // 자유 피드백

	public static Feedback create(Long userId, LocalDate weekStartDate, int cleaningAmountScore, int recommendedTimeScore, String comment) {
		if (comment == null || comment.isEmpty()) comment = "";
		return Feedback.builder()
			.userId(userId)
			.weekStartDate(weekStartDate)
			.cleaningAmountScore(cleaningAmountScore)
			.recommendedTimeScore(recommendedTimeScore)
			.comment(comment)
			.build();
	}
}
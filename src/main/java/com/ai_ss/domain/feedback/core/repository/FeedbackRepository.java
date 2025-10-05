package com.ai_ss.domain.feedback.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai_ss.domain.feedback.core.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}

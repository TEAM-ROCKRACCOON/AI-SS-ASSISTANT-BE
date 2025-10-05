package com.ai_ss.domain.cleaningTask.core;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai_ss.domain.cleaningTask.core.entity.CleaningTask;

public interface CleaningTaskRepository extends JpaRepository<CleaningTask, Long> {
}

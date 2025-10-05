package com.ai_ss.domain.todo.core.entity;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
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
@Table(name = "todo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;  // @ManyToOne 대신 userId 직접 저장

	private String title;

	private LocalDateTime startDateTime;  // 루틴 시작 일시

	private Boolean isDone = false;

	@Nullable
	private LocalDateTime completedAt;  // 완료된 경우 기록

	private Boolean isDeleted = false;

	@Nullable
	private LocalDateTime deletedAt;

	public static Todo create(Long userId, String title, LocalDateTime startDateTime) {
		return Todo.builder()
			.userId(userId)
			.title(title)
			.startDateTime(startDateTime)
			.isDone(false)
			.isDeleted(false)
			.build();
	}

	public void complete(boolean isDone) {
		this.isDone = isDone;
		this.completedAt = isDone ? LocalDateTime.now() : null;
	}

	public void delete() {
		this.isDeleted = true;
		this.deletedAt = LocalDateTime.now();
	}

	public void updateStartTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}
}

package com.ai_ss.domain.routine.core.entity;

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
@Table(name = "routine")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Routine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;  // @ManyToOne 대신 userId 직접 저장

	private String title;

	private LocalDateTime startDateTime;  // 루틴 시작 일시

	private Integer durationMinutes;  // 루틴 소요 시간 (분 단위)

	private Boolean completed;

	private LocalDateTime completedAt;  // 완료된 경우 기록

	private Boolean deleted;

	private LocalDateTime deletedAt;

	// 추후 필요 시 enum 타입이나 타입 분리 고려
	private String routineType;
}

package com.ai_ss.domain.todo.core.repository;

import com.ai_ss.domain.todo.core.entity.Todo;
import com.ai_ss.global.common.util.DateTimeUtil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

	/**
	 * 공통 코어 쿼리: 날짜 구간(start <= x < end)으로 시간순 조회
	 * - 예외 없이 빈 리스트 반환 (JPA 기본)
	 * - isDeleted=false 필터링 포함 (필요 없으면 메서드명에서 AndDeletedFalse 제거)
	 */
	List<Todo> findAllByUserIdAndIsDeletedFalseAndStartDateTimeBetweenOrderByStartDateTimeAsc(
		Long userId,
		LocalDateTime startInclusive,
		LocalDateTime endExclusive
	);

	/**
	 * [편의 메서드] 오늘자 Todo를 시간순으로 조회 (시스템 시계)
	 */
	default List<Todo> findTodayTodos(Long userId) {
		return findTodayTodos(userId, Clock.systemDefaultZone());
	}

	/**
	 * [편의 메서드] 오늘자 Todo를 시간순으로 조회 (테스트용 Clock 주입 가능)
	 */
	default List<Todo> findTodayTodos(Long userId, Clock clock) {
		LocalDate today = LocalDate.now(clock);
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.plusDays(1).atStartOfDay(); // [start, nextDay) 범위
		return findAllByUserIdAndIsDeletedFalseAndStartDateTimeBetweenOrderByStartDateTimeAsc(userId, start, end);
	}

	/**
	 * [편의 메서드] 주간(7일) Todo 조회
	 * - startDate(yyyy-MM-dd) **포함** 7일치 [start, start+7days)
	 * - 시간순 정렬
	 * - 파싱 실패 시 IllegalArgumentException 던짐
	 */
	default List<Todo> findTodosFor7DaysFrom(Long userId, String startDate) {
		LocalDate start = DateTimeUtil.parseIsoDate(startDate);
		LocalDateTime from = start.atStartOfDay();
		LocalDateTime to = start.plusDays(7).atStartOfDay(); // [from, to)
		return findAllByUserIdAndIsDeletedFalseAndStartDateTimeBetweenOrderByStartDateTimeAsc(userId, from, to);
	}

	/**
	 * 주어진 기간에 완료된 Todo를 날짜별로 그룹핑하여 카운트
	 * - 조건: isDeleted = false, isDone = true
	 * - 기간: [start, end)
	 */
	@Query("""
		select function('date', t.startDateTime) as day, count(t) as cnt
		from Todo t
		where t.userId = :userId
		  and t.isDeleted = false
		  and t.isDone = true
		  and t.startDateTime >= :start
		  and t.startDateTime < :end
		group by function('date', t.startDateTime)
		order by function('date', t.startDateTime) asc
		""")
	List<CountByDate> countCompletedByDateInRange(
		@Param("userId") Long userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

	// 날짜별 카운트 프로젝션
	interface CountByDate {
		LocalDate getDay();
		Long getCnt();
	}

	boolean existsByUserIdAndIsDeletedFalseAndStartDateTimeBetween(
		Long userId, LocalDateTime startInclusive, LocalDateTime endExclusive
	);
}

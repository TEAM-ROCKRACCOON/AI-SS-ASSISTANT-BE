package com.ai_ss.domain.todo.api.service;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai_ss.domain.cleaningTask.core.CleaningTaskRepository;
import com.ai_ss.domain.cleaningTask.core.entity.CleaningTask;
import com.ai_ss.domain.feedback.api.dto.response.WeeklyCountsResponse;
import com.ai_ss.domain.todo.api.dto.request.TodoIsDoneUpdateRequest;
import com.ai_ss.domain.todo.api.dto.request.TodoRequest;
import com.ai_ss.domain.todo.api.dto.request.TodoTimeUpdateRequest;
import com.ai_ss.domain.todo.api.dto.response.TodayTodoResponse;
import com.ai_ss.domain.todo.api.dto.response.WeekTodoResponse;
import com.ai_ss.domain.todo.core.entity.Todo;
import com.ai_ss.domain.todo.core.repository.TodoRepository;
import com.ai_ss.global.common.exception.AiSsException;
import com.ai_ss.global.common.exception.code.ErrorCode;
import com.ai_ss.global.common.util.DateTimeUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

	private final TodoRepository todoRepository;
	private final CleaningTaskRepository cleaningTaskRepository;

	@Transactional
	public TodayTodoResponse findTodayTodo(final Long userId) {
		LocalDate today = LocalDate.now(clock);
		seedTodosIfEmpty(userId, today);
		List<Todo> todayTodos = todoRepository.findTodayTodos(userId);
		return TodayTodoResponse.from(todayTodos);
	}

	@Transactional
	public WeekTodoResponse findWeekTodo(final Long userId, String startDate) {
		LocalDate start = DateTimeUtil.parseIsoDate(startDate);
		// 7일에 대해 각각 시드
		for (int i = 0; i < 7; i++) {
			seedTodosIfEmpty(userId, start.plusDays(i));
		}
		List<Todo> weekTodos = todoRepository.findTodosFor7DaysFrom(userId, startDate);
		return WeekTodoResponse.ofWeek(weekTodos, startDate);
	}

	@Transactional
	public void registerTodo(final Long userId, final TodoRequest request) {
		LocalDateTime todoTime = DateTimeUtil.mergeDateAndTime(request.date(), request.time());
		if (todoTime == null)
			throw new AiSsException(ErrorCode.INVALID_REQUEST_BODY);
		Todo todo = Todo.create(userId, request.title(), todoTime);
		todoRepository.save(todo);
	}

	@Transactional
	public void updateTodoTime(final Long userId, final Long todoId, final TodoTimeUpdateRequest request) {
		Todo todo = todoRepository.findById(todoId)
			.orElseThrow(() -> new AiSsException(ErrorCode.DATA_NOT_FOUND));
		assertOwner(todo, userId);

		LocalDateTime updatedDateTime = DateTimeUtil.combine(todo.getStartDateTime().toLocalDate(), request.time());

		todo.updateStartTime(updatedDateTime);
	}

	@Transactional
	public void completeTodo(final Long userId, final Long todoId, TodoIsDoneUpdateRequest request) {
		Todo todo = todoRepository.findById(todoId)
			.orElseThrow(() -> new AiSsException(ErrorCode.DATA_NOT_FOUND));
		assertOwner(todo, userId);
		todo.complete(request.isDone());
	}

	@Transactional
	public void deleteTodo(final Long userId, final Long todoId) {
		Todo todo = todoRepository.findById(todoId)
			.orElseThrow(() -> new AiSsException(ErrorCode.DATA_NOT_FOUND));
		assertOwner(todo, userId);
		todo.delete();
	}

	@Transactional(readOnly = true)
	public WeeklyCountsResponse getWeeklyCounts(Long userId, String weekStartDate) {
		// 유틸로 ISO date 파싱
		LocalDate start = DateTimeUtil.parseIsoDate(weekStartDate);
		LocalDateTime startDateTime = start.atStartOfDay();
		LocalDateTime endDateTime = start.plusDays(7).atStartOfDay();

		var results = todoRepository.countCompletedByDateInRange(userId, startDateTime, endDateTime);

		// 결과를 날짜별 카운트 맵으로 변환
		Map<LocalDate, Long> countByDate = new HashMap<>();
		for (TodoRepository.CountByDate r : results) {
			countByDate.put(r.getDay(), r.getCnt());
		}

		// 7일치 채우기 (없으면 0)
		Map<String, Integer> counts = new LinkedHashMap<>();
		for (int i = 0; i < 7; i++) {
			LocalDate day = start.plusDays(i);
			String dayKey = day.getDayOfWeek().name().substring(0, 3); // MON~SUN
			counts.put(dayKey, countByDate.getOrDefault(day, 0L).intValue());
		}

		return new WeeklyCountsResponse(counts);
	}

	private void assertOwner(Todo todo, Long userId) {
		if (!todo.getUserId().equals(userId)) {
			throw new AiSsException(ErrorCode.ACCESS_DENIED);
		}
	}

	// TODO : 추후 ai 모듈 도입 시 삭제 예정
	// === 랜덤 시드 유틸 ===
	private final Clock clock;

	private static final SecureRandom RND = new SecureRandom();
	private static final LocalTime SEED_START = LocalTime.of(8, 0);   // 08:00 시작
	private static final LocalTime SEED_END = LocalTime.of(21, 0);  // 21:00 이전
	private static final int SLOT_MINUTES = 30; // 30분 단위

	private LocalDateTime randomDateTimeOnDay(LocalDate day) {
		int totalSlots = (int)(Duration.between(SEED_START, SEED_END).toMinutes() / SLOT_MINUTES);
		int pick = RND.nextInt(Math.max(totalSlots, 1));
		LocalTime time = SEED_START.plusMinutes((long)pick * SLOT_MINUTES);
		return LocalDateTime.of(day, time);
	}

	private List<CleaningTask> pickRandomTasks(List<CleaningTask> pool, int count) {
		if (pool.isEmpty())
			return List.of();
		Collections.shuffle(pool, RND);
		return pool.subList(0, Math.min(count, pool.size()));
	}

	/**
	 * 해당 날짜에 유저 투두가 없으면 CleaningTask에서 1~3개 랜덤 생성 (idempotent)
	 */
	@Transactional
	protected void seedTodosIfEmpty(Long userId, LocalDate day) {
		LocalDateTime start = day.atStartOfDay();
		LocalDateTime end = day.plusDays(1).atStartOfDay();
		boolean exists = todoRepository.existsByUserIdAndIsDeletedFalseAndStartDateTimeBetween(userId, start, end);
		if (exists)
			return; // 이미 있으면 안 건드림

		List<CleaningTask> pool = cleaningTaskRepository.findAll();
		if (pool.isEmpty()) {
			log.warn("CleaningTask pool is empty. Skip seeding.");
			return;
		}

		int howMany = 1 + RND.nextInt(3); // 1~3
		List<CleaningTask> picked = pickRandomTasks(new ArrayList<>(pool), howMany);

		// 간단히 겹칠 확률 낮추려고 슬롯을 여러개 뽑아서 매칭
		Set<LocalDateTime> usedTimes = new HashSet<>();
		for (CleaningTask ct : picked) {
			LocalDateTime dt;
			int guard = 0;
			do {
				dt = randomDateTimeOnDay(day);
			} while (!usedTimes.add(dt) && ++guard < 10);

			Todo todo = Todo.create(userId, ct.getTitle(), dt);
			todoRepository.save(todo);
		}
		todoRepository.flush();
	}
}

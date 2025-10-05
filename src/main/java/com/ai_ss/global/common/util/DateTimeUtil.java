package com.ai_ss.global.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateTimeUtil {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter TIME_FORMAT_12H = DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.ENGLISH);
	private static final DateTimeFormatter TIME_FORMAT_24H = DateTimeFormatter.ofPattern("HH:mm");
	private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_DATE;

	private DateTimeUtil() {
	}

	// ---------- [ Parse: String → LocalDate / LocalDateTime / LocalTime ] ----------

	/**
	 * "2025-10-05" → LocalDate
	 */
	public static LocalDate parseDate(String dateStr) {
		if (dateStr == null || dateStr.isBlank()) return null;
		return LocalDate.parse(dateStr, DATE_FORMAT);
	}

	/**
	 * "09:00 AM" or "21:30" → LocalTime
	 */
	public static LocalTime parseTime(String timeStr) {
		if (timeStr == null || timeStr.isBlank()) return null;

		try {
			return LocalTime.parse(timeStr, TIME_FORMAT_12H); // "09:00 AM"
		} catch (Exception e) {
			return LocalTime.parse(timeStr, TIME_FORMAT_24H); // "21:30"
		}
	}

	/**
	 * Merge date + time → LocalDateTime
	 */
	public static LocalDateTime mergeDateAndTime(String dateStr, String timeStr) {
		LocalDate date = parseDate(dateStr);
		LocalTime time = parseTime(timeStr);
		if (date == null || time == null) return null;
		return LocalDateTime.of(date, time);
	}

	// ---------- [ Format: LocalDateTime → String ] ----------

	/**
	 * LocalDateTime → "yyyy-MM-dd"
	 */
	public static String formatDate(LocalDateTime dateTime) {
		if (dateTime == null) return null;
		return dateTime.format(DATE_FORMAT);
	}

	/**
	 * LocalDateTime → "HH:mm" (24시간제)
	 */
	public static String formatTime(LocalDateTime dateTime) {
		if (dateTime == null) return null;
		return dateTime.format(TIME_FORMAT_24H);
	}

	/**
	 * LocalDate → "yyyy-MM-dd"
	 */
	public static String formatDate(LocalDate date) {
		if (date == null) return null;
		return date.format(DATE_FORMAT);
	}

	/**
	 * LocalTime → "HH:mm"
	 */
	public static String formatTime(LocalTime time) {
		if (time == null) return null;
		return time.format(TIME_FORMAT_24H);
	}

	// ---------- [ Parse ISO: String → LocalDate ] ----------

	/**
	 * "2025-10-05" (ISO_DATE) → LocalDate
	 */
	public static LocalDate parseIsoDate(String isoDate) {
		if (isoDate == null || isoDate.isBlank()) return null;
		return LocalDate.parse(isoDate, ISO_DATE);
	}

	/**
	 * "hh:mm a" 또는 "HH:mm" 문자열을 LocalTime으로 유연하게 파싱
	 */
	public static LocalTime parseFlexibleTime(String time) {
		try {
			return LocalTime.parse(time.trim(), TIME_FORMAT_12H);
		} catch (DateTimeParseException e1) {
			try {
				return LocalTime.parse(time.trim(), TIME_FORMAT_24H);
			} catch (DateTimeParseException e2) {
				throw new IllegalArgumentException("Invalid time format: " + time);
			}
		}
	}

	/**
	 * LocalDate + time 문자열 → LocalDateTime으로 합성
	 */
	public static LocalDateTime combine(LocalDate date, String time) {
		return LocalDateTime.of(date, parseFlexibleTime(time));
	}
}

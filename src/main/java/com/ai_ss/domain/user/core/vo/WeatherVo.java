package com.ai_ss.domain.user.core.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WeatherVo {

	private final double temperature; // 섭씨
	private final int humidity;       // %
	private final double windSpeed;   // m/s
	private final String description; // "맑음", "비", "흐림" 등
	private final double dustLevel;   // 미세먼지 수치 (μg/m³)
	private final boolean isRain;     // 비 여부
	private final String sourceTime;  // API 기준 시각 (예: 2025-06-19T10:00)

}

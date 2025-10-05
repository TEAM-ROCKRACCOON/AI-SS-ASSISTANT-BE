package com.ai_ss.domain.user.core.entity.enums;

import java.util.Locale;

import com.ai_ss.global.common.exception.AiSsException;
import com.ai_ss.global.common.exception.code.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PreferredDay {
	MON("월"),
	TUE("화"),
	WED("수"),
	THU("목"),
	FRI("금"),
	SAT("토"),
	SUN("일");

	private final String labelKo;

	PreferredDay(String labelKo) {
		this.labelKo = labelKo;
	}

	@JsonValue // Jackson 직렬화 시 "월" 같은 라벨로 출력하고 싶을 때 유용 (원하면 제거)
	public String getLabelKo() {
		return labelKo;
	}

	// 영문 코드(MON~SUN) 또는 한글 라벨(월~일) 모두 수용
	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static PreferredDay from(String value) {
		if (value == null) return null;
		String v = value.trim();

		// 1) 영문 코드 허용 (대소문자 무시)
		try {
			return PreferredDay.valueOf(v.toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException ignore) {
			// pass
		}

		// 2) 한글 라벨 허용
		for (PreferredDay d : values()) {
			if (d.labelKo.equals(v)) {
				return d;
			}
		}

		throw new AiSsException(ErrorCode.INVALID_REQUEST_BODY);
	}
}

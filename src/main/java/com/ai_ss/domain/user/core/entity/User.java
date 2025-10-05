package com.ai_ss.domain.user.core.entity;

import java.util.HashSet;
import java.util.Set;

import com.ai_ss.domain.user.core.entity.enums.PreferredDay;
import com.ai_ss.domain.user.core.entity.enums.Role;
import com.ai_ss.domain.user.core.entity.enums.SocialType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String socialId; // Google OAuth 고유 식별자

	private SocialType socialType;

	private Role role; // e.g. USER, ADMIN

	private String nickname;

	private String roadAddressName;

	private String placeDetailAddress;

	private double latitude;

	private double longitude;

	private String cleaningFrequency;

	private String cleaningDistribution;

	private String preferredTimeRange;

	/**
	 * 선호 요일: Enum 컬렉션으로 분리 테이블에 저장
	 * 테이블명: users_preferred_days
	 * 컬럼: user_id, getDay
	 */
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
		name = "users_preferred_days",
		joinColumns = @JoinColumn(name = "user_id")
	)
	@Enumerated(EnumType.STRING)
	@Column(name = "getDay", nullable = false, length = 8)
	private Set<PreferredDay> preferredDays = new HashSet<>();

	private String itemQuantity;

	private int sleepAt;

	private int wakeAt;

	private String email;

	public static User create(String socialId, SocialType socialType, Role role, String nickname, String email) {
		return User.builder()
			.socialId(socialId)
			.socialType(socialType)
			.role(role)
			.nickname(nickname)
			.email(email)
			.build();
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setAddress(String roadAddressName, String placeDetailAddress,
		double latitude, double longitude) {
		this.roadAddressName = roadAddressName;
		this.placeDetailAddress = placeDetailAddress;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void setHabit(String cleaningFrequency, String cleaningDistribution,
		String preferredTimeRange, Set<PreferredDay> preferredDays,
		String itemQuantity, int sleepAt, int wakeAt) {
		this.cleaningFrequency = cleaningFrequency;
		this.cleaningDistribution = cleaningDistribution;
		this.preferredTimeRange = preferredTimeRange;
		this.preferredDays.clear();
		if (preferredDays != null) {
			this.preferredDays.addAll(preferredDays);
		}
		this.itemQuantity = itemQuantity;
		this.sleepAt = sleepAt;
		this.wakeAt = wakeAt;
	}

	public void updateProfile(String nickname, String email) {
		this.nickname = nickname;
		this.email = email;
	}
}

package com.ai_ss.domain.user.core.entity;

import com.ai_ss.domain.user.core.entity.enums.Role;
import com.ai_ss.domain.user.core.entity.enums.SocialType;
import com.ai_ss.domain.user.core.entity.valueobject.Address;
import com.ai_ss.domain.user.core.entity.valueobject.CleaningPreference;

import jakarta.persistence.Embedded;
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
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String oauthId; // Google OAuth 고유 식별자
	private String nickname;

	@Embedded
	private Address address;

	@Embedded
	private CleaningPreference cleaningPreference;

	private String email;
	private String profileImageUrl;

	private Role role; // e.g. USER, ADMIN

	private SocialType socialType;
}

package com.ai_ss.domain.user.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai_ss.domain.user.api.dto.request.AddressRequest;
import com.ai_ss.domain.user.api.dto.request.HabitRequest;
import com.ai_ss.domain.user.api.dto.request.ProfileUpdateRequest;
import com.ai_ss.domain.user.api.dto.response.ProfileResponse;
import com.ai_ss.domain.user.core.entity.User;
import com.ai_ss.domain.user.core.entity.enums.PreferredDay;
import com.ai_ss.domain.user.core.entity.enums.Role;
import com.ai_ss.domain.user.core.entity.enums.SocialType;
import com.ai_ss.domain.user.core.repository.UserRepository;
import com.ai_ss.global.auth.client.dto.UserSocialInfoResponse;
import com.ai_ss.global.common.exception.AiSsException;
import com.ai_ss.global.common.exception.code.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private static final String TERM_URL = "url";

	@Transactional
	public Long registerUser(final UserSocialInfoResponse userSocialInfoResponse) {
		String socialId = userSocialInfoResponse.socialId();
		SocialType socialType = userSocialInfoResponse.socialType();
		String name = userSocialInfoResponse.name();
		String email = userSocialInfoResponse.email();

		User user = User.create(socialId, socialType, Role.USER, name, email);
		userRepository.save(user);

		return user.getId();
	}

	@Transactional(readOnly = true)
	public User findUserBySocialIdAndSocialType(final String socialId, final SocialType socialType) {
		return userRepository.findBySocialTypeAndSocialId(socialId, socialType)
			.orElse(null);
	}

	@Transactional
	public void registerNickname(final Long userId, final String nickname) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AiSsException(ErrorCode.USER_NOT_FOUND));
		user.setNickname(nickname);
		userRepository.save(user);
	}

	@Transactional
	public void registerAddress(final Long userId, final AddressRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AiSsException(ErrorCode.USER_NOT_FOUND));
		user.setAddress(request.roadAddressName(), request.placeDetailAddress(), request.latitude(), request.longitude());
		userRepository.save(user);
	}

	@Transactional
	public void registerHabit(final Long userId, final HabitRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AiSsException(ErrorCode.USER_NOT_FOUND));
		Set<PreferredDay> days = toSet(request.preferredDays());

		user.setHabit(
			request.cleaningFrequency(),
			request.cleaningDistribution(),
			request.preferredTimeRange(),
			days,
			request.itemQuantity(),
			request.sleepAt(),
			request.wakeAt()
			);
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public ProfileResponse getProfile(final Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AiSsException(ErrorCode.USER_NOT_FOUND));
		return ProfileResponse.of(user.getNickname(), user.getEmail(), TERM_URL);
	}

	@Transactional
	public void updateProfile(final Long userId, final ProfileUpdateRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AiSsException(ErrorCode.USER_NOT_FOUND));
		user.updateProfile(request.nickname(), request.email());
	}

	@Transactional
	public void withdraw(final Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new AiSsException(ErrorCode.USER_NOT_FOUND));
		userRepository.delete(user);
	}

	private static Set<PreferredDay> toSet(List<PreferredDay> list) {
		return (list == null) ? new HashSet<>() : new HashSet<>(list);
	}
}

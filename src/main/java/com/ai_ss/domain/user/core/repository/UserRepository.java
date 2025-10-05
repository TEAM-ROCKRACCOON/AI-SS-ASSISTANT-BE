package com.ai_ss.domain.user.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ai_ss.domain.user.core.entity.User;
import com.ai_ss.domain.user.core.entity.enums.SocialType;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.socialId = :socialId AND u.socialType = :socialType")
	Optional<User> findBySocialTypeAndSocialId(@Param("socialId") String socialId,
		@Param("socialType") SocialType socialType);
}

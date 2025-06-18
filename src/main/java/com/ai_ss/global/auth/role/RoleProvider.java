package com.ai_ss.global.auth.role;

import com.ai_ss.domain.user.core.entity.enums.Role;

public interface RoleProvider {
	boolean supports(String authenticationType);
	Role provideRole(Long id);
}

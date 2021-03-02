package com.ys.photoapp.api.users.ui.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ys.photoapp.api.users.shared.UsersDto;

public interface UsersService extends UserDetailsService {
	UsersDto createUser(UsersDto userDetails);

	UsersDto getUserDetailsByEmail(String email);

	UsersDto getUserByUserId(String userId);
}

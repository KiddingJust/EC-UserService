package com.gaiga.userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.gaiga.userservice.dto.UserDto;
import com.gaiga.userservice.repository.UserEntity;


public interface UserService extends UserDetailsService{
	UserDto createUser(UserDto userDto);
	
 	UserDto getUserByUserId(String userId);
	Iterable<UserEntity> getUserByAll();

	UserDto getUserDetailsByEmail(String email);
	
}

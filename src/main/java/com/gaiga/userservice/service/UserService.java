package com.gaiga.userservice.service;

import com.gaiga.userservice.dto.UserDto;
import com.gaiga.userservice.repository.UserEntity;

public interface UserService {
	UserDto createUser(UserDto userDto);
	
 	UserDto getUserByUserId(String userId);
	Iterable<UserEntity> getUserByAll();
	
}

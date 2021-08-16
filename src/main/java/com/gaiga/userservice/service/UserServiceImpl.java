package com.gaiga.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gaiga.userservice.dto.UserDto;
import com.gaiga.userservice.repository.UserEntity;
import com.gaiga.userservice.repository.UserRepository;
import com.gaiga.userservice.vo.ResponseOrder;

@Service
public class UserServiceImpl implements UserService {

	UserRepository userRepository;
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDto createUser(UserDto userDto) {
		
		userDto.setUserId(UUID.randomUUID().toString());
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
		userRepository.save(userEntity);
	
		UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
		
		return returnUserDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null) {
			throw new UsernameNotFoundException("user not found");
		}
		
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		
		List<ResponseOrder> order = new ArrayList<>();
		userDto.setOrders(order);
		
		return userDto;
	}

	@Override
	public Iterable<UserEntity> getUserByAll() {
		return userRepository.findAll();
	}
	


}

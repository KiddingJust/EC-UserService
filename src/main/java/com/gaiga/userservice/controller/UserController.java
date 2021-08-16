package com.gaiga.userservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gaiga.userservice.dto.UserDto;
import com.gaiga.userservice.repository.UserEntity;
import com.gaiga.userservice.service.UserService;
import com.gaiga.userservice.vo.Greeting;
import com.gaiga.userservice.vo.RequestUser;
import com.gaiga.userservice.vo.ResponseUser;

@RestController
@RequestMapping(value="/user-service")
public class UserController {
	
	private Environment env;
	private UserService userService;
	
	@Autowired
	private Greeting greeting;
	
	@Autowired
	public UserController(Environment env, UserService userService) {
		this.env = env;
		this.userService = userService;
	}

	//environment 객체 주입받았으므로 다른 식으로도 가능. 
//	@GetMapping("/health_check")
//	public String status(HttpServletRequest request) {
////		return "It's Working in User Service";
//		return String.format("It's Working in User Service on Port %s", request.getServerPort());
//	}
	@GetMapping("/health_check")
	public String status() {
//		return "It's Working in User Service";
		return String.format("It's Working in User Service on Port %s", 
				env.getProperty("local.server.port"));
	}
	
	@GetMapping("/welcome")
	public String welcome() {
//		return env.getProperty("greeting.message");
		//이건 Greetings.java의 @Value 통해 가능.  
		return greeting.getMessage();
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<ResponseUser>> getUsers(){
		Iterable<UserEntity> userList = userService.getUserByAll();
		
		List<ResponseUser> result = new ArrayList<>();
		userList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseUser.class));
		});
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PostMapping("/users")
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
		
		//다른 클래스로 넘기기 위해서는 Dto로 변경 먼저 필요. 
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = mapper.map(user, UserDto.class);		
		userService.createUser(userDto);
		
		ResponseUser responseUser = mapper.map(userDto,  ResponseUser.class);
		
//		return "Create user method is called";
 		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseUser> getUsers(@PathVariable("userId") String userId){
		UserDto userDto = userService.getUserByUserId(userId);
		
		ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);
		
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
}

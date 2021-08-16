package com.gaiga.userservice.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
//@AllArgsConstructor	//argument 갖고 있는 생성자 만들어주겠다. (no는 argument 없는생성자)
public class Greeting {
	
	@Value("${greeting.message}")
	private String message;
}

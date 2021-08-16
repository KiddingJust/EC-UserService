package com.gaiga.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	//일단 인증은 했다 치고 권한 부분만 설정
	///users 라는 preFix 붙은 url은 인증 없이 사용 가능하도록! 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.authorizeRequests()
					.antMatchers("/users/**",
								 "/user-service/**")
					.permitAll();
	
		http.headers().frameOptions().disable();
	}
}

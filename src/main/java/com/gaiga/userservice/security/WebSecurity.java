package com.gaiga.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gaiga.userservice.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private UserService userService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private Environment env;
	
	public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.env = env;
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	//일단 인증은 했다 치고 권한 부분만 설정
	///users 라는 preFix 붙은 url은 인증 없이 사용 가능하도록! 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
//		http.authorizeRequests()
//					.antMatchers("/users/**",
//								 "/user-service/**")
//					.permitAll();
		//인증이 된 상태에서만 통과시키도록. 
		http.authorizeRequests()
				.antMatchers("/**")
				.hasIpAddress("172.30.123.121")	//source ip
				.and()
				.addFilter(getAuthenticationFilter());
				
		http.headers().frameOptions().disable();
	}

	//앞에서 만든 AuthenticationFilter.java
	//이를 통해 AuthenticationFilter를 빈으로 등록해서 사용하는 게 아니라,
	//Spring Security에서 인스턴스를 직접 생성해서 사용하고 있음. 
	private AuthenticationFilter getAuthenticationFilter() throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, env);
		authenticationFilter.setAuthenticationManager(authenticationManager());
		
		return authenticationFilter;
	}

   //인증 . 데이터베이스의 pw(encrypted)와 사용자 입력 pw 비교작업. 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//정확히 어떻게 하는지 모르겠... 
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	
}

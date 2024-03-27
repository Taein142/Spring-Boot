package com.icia.board.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.icia.board.util.SessionIntercepter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	// 세션 인터셉트 클래스를 자동으로 등록
	@Autowired
	private SessionIntercepter intercepter;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(intercepter) // 인터셉터 객체 등록
				.addPathPatterns("/**") // 인터셉터 범위
				.excludePathPatterns("/", "/css/**", "/cj/**", "/images/**") // 인터셉터에서 제외할 요소들
				.excludePathPatterns("/joinForm", "/loginForm", "idCheck")
				.excludePathPatterns("/joinProc", "/loginProc", "/error//**")
				.excludePathPatterns("/authUser", "/mailConfirm", "/codeAuth")
				.excludePathPatterns("/pwdChange", "/pwdChangeProc");
	}
}

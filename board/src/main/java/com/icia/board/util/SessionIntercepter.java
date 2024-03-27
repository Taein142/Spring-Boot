package com.icia.board.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SessionIntercepter implements AsyncHandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 로그인 전 인터셉트
		log.info("preHandle()");
		// 세션
		HttpSession session = request.getSession();
		
		if (session.getAttribute("member") == null) {
			// 로그인 하지 않은 상태
			log.info("인터셉트! - 로그인 안함");
			response.sendRedirect("/");
			return false;
		}
	
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 로그아웃 한 후에 인터셉트
		log.info("postHandle()");
		
		// http 버전에 따라 처리 방식이 다름(http 1.0과 1.1 혼용 중)
		if(request.getProtocol().equals("HTTP/1.1")) {
			response.setHeader("Cache-Control", "no-cashe, no-store, must-revalidate");
		}else {
			response.setHeader("Pragma", "no-cache");
		}
	}
}

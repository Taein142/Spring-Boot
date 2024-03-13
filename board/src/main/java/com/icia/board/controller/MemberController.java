package com.icia.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.board.dto.MemberDTO;
import com.icia.board.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;

	@GetMapping("/")
	public String home() {
		log.info("home()");
		return "index";
	}
	
	@GetMapping("loginForm")
	public String login() {
		log.info("loginForm()");
		return "loginForm";
	}
	
	@PostMapping("loginProc")
	public String loginProc(MemberDTO member, HttpSession session, RedirectAttributes rttr) {
		log.info("loginProc()");
		
		return memberService.loginProc(member, session, rttr);
	}
}

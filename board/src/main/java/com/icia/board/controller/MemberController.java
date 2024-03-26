package com.icia.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.board.dto.MemberDto;
import com.icia.board.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class MemberController {
	
	@Autowired
	private MemberService mServ;

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
	public String loginProc(MemberDto member, HttpSession session, RedirectAttributes rttr) {
		log.info("loginProc()");
		
		return mServ.loginProc(member, session, rttr);
	}
	
	@GetMapping("joinForm")
	public String join() {
		log.info("joinForm()");
		return "joinForm";
	}

//	idCheck 등 ajax는 BoardRestContoller에서 하는 중
//	@GetMapping("idCheck")
//	@ResponseBody
//	public String idCheck(@RequestParam("mid") String mid) {
//		log.info("idcheck(){}", mid);
//		
//		return "ok"; // javascript ajax success의 res로 들어가는 값.
//	}
	
	@PostMapping("joinProc")
	public String joinProc(MemberDto memberDTO, RedirectAttributes rttr) {
		log.info("joinProc");
		String view = mServ.memberJoin(memberDTO, rttr);
		
		return view;
	}
	
	// 메일 인증 매핑 메소드
	@GetMapping("authUser")
	public String authUser() {
		log.info("authUser");
		return "authUser";
	}
	
	@GetMapping("pwdChange")
	public String pwdChange() {
		log.info("pwdChange()");
		return "pwdChange";
	}
	
	@PostMapping("pwdChangeProc")
	public String pwdChangeProc(MemberDto memberDTO, HttpSession session, RedirectAttributes rttr) {
		log.info("pwdChangeProc()");
		
		String view = mServ.pwdChangeProc(memberDTO, session, rttr);
		
		return view;
	}
	
	@GetMapping("logout")
	public String logout(HttpSession session, RedirectAttributes rttr) {
		log.info("logout()");
		
		String view = mServ.logout(session, rttr);
		return view;
	}
}

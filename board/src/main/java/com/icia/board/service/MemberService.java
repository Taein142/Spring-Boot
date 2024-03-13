package com.icia.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.board.dao.MemberDao;
import com.icia.board.dto.MemberDTO;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberService {
	@Autowired
	private MemberDao mDao;
	
	// 로그인 처리 메소드
	public String loginProc(MemberDTO member, HttpSession session, RedirectAttributes rttr) {
		log.info("loginProc()");
		
		String view = null;
		String msg = null;
		
		String encPwd = mDao.selectPassword(member.getM_id());
		
		if (encPwd != null) { // member가 존재함
			if (encPwd.equals(member.getM_pwd())) {
				// 로그인 성공
				member = mDao.selectMember(member.getM_id());
				//세션에 로그인한 회원의 정보를 저장
				session.setAttribute("member", member);
				// 로그인 성공시 이동할 화면(view) 지정
				view = "redirect:boardList?pageNum=1";
				msg = "로그인 성공";
			}else {
				msg = "비밀번호가 틀립니다.";
				view = "redirect:loginForm";
			}
		}else { // member가 존재하지 않음
			msg = "존재하지 않는 아이디입니다.";
			view = "redirect:loginForm";
		}
		
		rttr.addFlashAttribute("msg", msg);
		return view;
	}
}

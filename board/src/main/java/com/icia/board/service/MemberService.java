package com.icia.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.board.dao.MemberDao;
import com.icia.board.dto.MemberDto;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberService {
	@Autowired
	private MemberDao mDao;
	
	//비밀번호 암호화 인코더
	private BCryptPasswordEncoder pEncoder = 
			new BCryptPasswordEncoder();
	
	//로그인 처리 메소드
	public String loginProc(MemberDto member,
							HttpSession session,
							RedirectAttributes rttr) {
		log.info("loginProc()");
		
		String view = null;
		String msg = null;
		
		String encPwd = mDao.selectPassword(member.getM_id());
		
		if(encPwd != null) { //member가 존재함
			//matches(평문-사용자입력값, 암호문-DB저장값)
			if(pEncoder.matches(member.getM_pwd(), encPwd)) {
				//로그인 성공!
				member = mDao.selectMember(member.getM_id());
				//세션에 로그인한 회원의 정보를 저장
				session.setAttribute("member", member);
				//로그인 성공 시 이동할 화면(view) 지정
				view = "redirect:boardList?pageNum=1";
				msg = "로그인 성공";
			}
			else {
				msg = "비밀번호가 틀립니다.";
				view = "redirect:loginForm";
			}
		}
		else { //member가 없음
			msg = "존재하지 않는 아이디입니다.";
			view = "redirect:loginForm";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return view;
	}
	
	public String idCheck(String mid) {
		log.info("idCheck()");
		String result = null;
		
		int memCnt = mDao.selectId(mid);
		if(memCnt == 0) {
			result = "ok";
		} else {
			result = "fail";
		}
		
		return result;
	}
	
	public String memberJoin(MemberDto member,
							 RedirectAttributes rttr) {
		log.info("memberJoin()");
		//가입 성공 시 첫페이지(또는 로그인페이지)로, 실패 시 가입 페이지로 이동
		String view = null;
		String msg = null;
		
		//비밀번호 암호화 처리
		String encPwd = pEncoder.encode(member.getM_pwd());
		log.info(encPwd);
		member.setM_pwd(encPwd);//암호화된 비밀번호 다시 저장.
		
		try {
			mDao.insertMember(member);
			view = "redirect:/";
			msg = "가입 성공";
		} catch (Exception e) {
			e.printStackTrace();
			view = "redirect:joinForm";
			msg = "가입 실패";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return view;
	}
	
	public String pwdChangeProc(MemberDto member, 
								HttpSession session,
								RedirectAttributes rttr) {
		log.info("pwdChangeProc()");
		String view = null;
		String msg = null;
		
		String m_id = (String) session.getAttribute("m_id");
		String encPwd = pEncoder.encode(member.getM_pwd());
		
		if(m_id != null) {
			member.setM_id(m_id);			
			member.setM_pwd(encPwd);
			
			try {
				mDao.updatePassword(member);
				msg = "비밀번호 변경 성공";
				view = "redirect:loginForm";
			} catch (Exception e) {
				e.printStackTrace();
				msg = "비밀번호 변경 실패";
				view = "redirect:pwdChange";
			}
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return view;
	}

	public String logout(HttpSession session, RedirectAttributes rttr) {
		log.info("logout()");
		session.invalidate();
		rttr.addFlashAttribute("msg", "로그아웃되었습니다.");
		return "redirect:/";
	}
	
}//class end






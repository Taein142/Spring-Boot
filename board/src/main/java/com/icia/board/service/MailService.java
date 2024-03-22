package com.icia.board.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.icia.board.dao.MemberDao;
import com.icia.board.dto.MemberDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailService {
	// 메일 전송 객체
	@Autowired
	private JavaMailSender emailSender;

	// Thymeleaf 사용을 위한 객체의 의존성 주입
	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private MemberDao mDao;

	private String authCode; // 인증코드 저장 변수

	// 인증코드 생성 메소드
	public void createCode() {
		Random random = new Random();
		StringBuffer key = new StringBuffer();

		// 8자 코드 생성
		for (int i = 0; i < 8; i++) {
			int index = random.nextInt(3);

			switch (index) {
			case 0:
				key.append((char) (random.nextInt(26)) + 97);
				break;
			case 1:
				key.append((char) (random.nextInt(26) + 65));
				break;
			case 2: 
				key.append((char)(random.nextInt(9) + 48));
				break;
			default:
				break;
			}
		}
		authCode = key.toString();
	}
	
	// 메일 양식 생성 메소드
	public MimeMessage createEmailForm(String email) 
			throws MessagingException, UnsupportedEncodingException{
		log.info("createEmailForm()");
		
//		String setFrom = "naver_id@naver.com";
		String setFrom = "xodlsdldy@naver.com"; // mail seeting에 설정한 메일 주소
		String title = "인증 코드 전송"; // 메일 제목
		
		MimeMessage message = emailSender.createMimeMessage();
		message.addRecipients(MimeMessage.RecipientType.TO, email); // 받는사람
		message.setSubject(title);
		message.setFrom(setFrom);
		message.setText(setContext(), "UTF-8", "html"); // 메일 내용
		
		return message;
	}
	
	// Thymeleaf를 이용한 context(HTML, 메일화면)를 설정하는 메소드
	private String setContext() {
		log.info("setContext()");
		createCode();// 인증코드 생성
		Context context = new Context();
		context.setVariable("code", authCode);
		return templateEngine.process("mailForm", context);
	}
	
	// 메일 전송 메소드
	public String sendEmail(MemberDTO memberDTO, HttpSession session) {
		log.info("sendEmail()");
		
		MimeMessage emailForm = null;
		String res = null;
		String email = null;
		
		try {
			email = mDao.selectEmail(memberDTO.getM_id());
			if (email.equals(memberDTO.getM_email())) {
				emailForm = createEmailForm(email);
				log.info(email);
				emailSender.send(emailForm); // 메일 전송
				res = "ok";
				// 코드값을 세션에 저장
				session.setAttribute("authCode", authCode);
				session.setAttribute("m_id", memberDTO.getM_id());
				
			}else {
				res = "fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			res = "fail";
		}
		return res;
	}

	public String codeAuth(String v_code, HttpSession session) {
		log.info("codeAuth()");
		String authCode = (String) session.getAttribute("authCode");
		String res = null;
		
		if (authCode != null) {			
			if (v_code.equals(authCode)) {
				res = "ok";
				log.info("코드가 일치합니다.");
			}else {
				res = "fail";
				log.info("코드가 틀렸습니다.");
			}
		}else {
			res = "fail";
			log.info("코드가 비어있습니다.");
		}
		
		session.removeAttribute("authCode");
		
		return res;
	}
}

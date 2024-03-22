package com.icia.board.dao;

import org.apache.ibatis.annotations.Mapper;

import com.icia.board.dto.MemberDTO;

@Mapper
public interface MemberDao {
	// 로그인 비밀번호 가져오는 메소드
	String selectPassword(String mid);

	// 로그인 성공 후 회원 정보를 가져오는 메소드
	MemberDTO selectMember(String mid);

	// idCheck용 메소드
	int selectId(String mid);
	
	// 회원 가입 메소드
	void insertMember(MemberDTO memberDTO);
	
	// 메일 주소를 가져오는 메소드
	String selectEmail(String m_id);
	
	// 비밀번호 변경 메소드
	void updatePassword(MemberDTO memberDTO);
}

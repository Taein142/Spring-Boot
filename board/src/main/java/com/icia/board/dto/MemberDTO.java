package com.icia.board.dto;

import lombok.Data;

@Data
public class MemberDTO {
	private String m_id;
	private String m_pwd;
	private String m_name;
	private String m_email;
	private String m_birth;
	private String m_addr;
	private String m_phone;
	private int m_point;
	private String g_name;
}

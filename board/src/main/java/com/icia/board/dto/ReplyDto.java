package com.icia.board.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyDto {
	private int r_num;//댓글번호
	private int r_bnum;//게시글 번호
	private String r_contents;
	private String r_id;//작성자 id
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp r_date;
}
//@JsonFormat
//  ajax(RESTful)로 날짜를 전송할 때 날짜 형식을 지정할 수 있다.


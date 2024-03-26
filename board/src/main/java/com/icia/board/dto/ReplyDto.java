package com.icia.board.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyDto {
	private int r_num; // 댓글 번호
	private int r_bnum; // 개시글 번호
	private String r_contents;
	private String r_id; // 작성자 id
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp r_date;
}

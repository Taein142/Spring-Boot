package com.icia.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
	private String colname;//제목, 작성자 검색용 컬럼명
	private String keyword;//검색어
	private int pageNum;
	private int listCnt;
}

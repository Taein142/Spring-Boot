package com.icia.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.icia.board.dao.BoardDao;
import com.icia.board.dto.BoardDto;
import com.icia.board.dto.SearchDto;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardService {
	@Autowired
	private BoardDao bDao;
	
	private int lCnt = 10; // 한 화면(페이지)에 보여질 게시글 개수
	
	public String getBoardList(SearchDto sDto, HttpSession session, Model model) {
		log.info("getBoardList");
		String view = "boardList";
		
		// DB에서 게시글 목록 가져오기
		int num = sDto.getPageNum();
		
		if (sDto.getListCnt() == 0) {
			sDto.setListCnt(lCnt); // 목록 개수 값 설정(초기 값 10개)
		}
		
		sDto.setPageNum((num-1) * sDto.getListCnt());
		List<BoardDto> bList = bDao.seleBoardList(sDto);
		model.addAttribute("bList", bList);
		
		// 페이징 처리
		
		
		// 페이지 관련 내용 세션에 저장
		
		return view;
	}

}

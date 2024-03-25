package com.icia.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.icia.board.dto.SearchDto;
import com.icia.board.service.BoardService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardController {
	@Autowired
	private BoardService bServ;
	
	@GetMapping("boardList")
	public String showBoardList(SearchDto sDto, HttpSession session, Model model) {
		log.info("showBoardList()");
		
		String view = bServ.getBoardList(sDto, session, model);
		
		return view;
	}

}

package com.icia.board.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.board.dto.BoardDto;
import com.icia.board.dto.BoardFileDto;
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
	public String boardList(SearchDto sdto,
							HttpSession sessioin, 
							Model model) {
		log.info("boardList()");
		String view = bServ.getBoardList(sdto, sessioin, model);
		//String view = "boardList";
		return view;
	}
	
	@GetMapping("writeForm")
	public String writeForm() {
		log.info("writeForm()");
		return "writeForm";
	}
	
	@PostMapping("writeProc")
	public String writeProc(@RequestPart("files") List<MultipartFile> files,
							BoardDto board,
							HttpSession session,
							RedirectAttributes rttr) {
		log.info("writeProc()");
		String view = bServ.boardWrite(files, board, session, rttr);
		return view;
	}
	
	@GetMapping("boardDetail")
	public String boardDetail(@RequestParam("b_num") int b_num, HttpSession session,Model model) {
		log.info("boardDetail()");
		String view = bServ.getBoard(b_num, session, model);
		return view;
	}
	
	@GetMapping("download")
	public ResponseEntity<Resource> fileDownload (BoardFileDto bfile,
												  HttpSession session) 
												  throws IOException {
		log.info("fileDownload()");
		ResponseEntity<Resource> resp = bServ.fileDownload(bfile, session);
		return resp;
	}
	
	@GetMapping("updateForm")
	public String updateForm(@RequestParam("b_num") int b_num, Model model) {
		log.info("updateForm()");
		String view = bServ.updateBoard(b_num, model);
		return view;
	}
	
	@PostMapping("updateProc")
	public String updateProc(@RequestPart("files") List<MultipartFile> files,
							 BoardDto board,
							 HttpSession session,
							 RedirectAttributes rttr) {
		log.info("updateProc()");
		String view = bServ.boardUpdate(files, board, session, rttr);
		return view;
	}
	
	@GetMapping("boardDelete")
	public String boardDelete(@RequestParam("b_num") int b_num, HttpSession session, RedirectAttributes rttr) {
		log.info("boardDelete");
		
		String view = bServ.boardDelete(b_num, session, rttr);
		
		return view;
	}
}//class end

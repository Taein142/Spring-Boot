package com.icia.board.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.board.dao.BoardDao;
import com.icia.board.dao.MemberDao;
import com.icia.board.dto.BoardDto;
import com.icia.board.dto.BoardFileDto;
import com.icia.board.dto.MemberDto;
import com.icia.board.dto.ReplyDto;
import com.icia.board.dto.SearchDto;
import com.icia.board.util.PagingUtil;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardService {
	@Autowired
	private BoardDao bDao;
	@Autowired
	private MemberDao mDao;

	// 트랜젝션 관련
	@Autowired
	private PlatformTransactionManager manager;
	@Autowired
	private TransactionDefinition definition;

	private int lCnt = 10; // 한 화면(페이지)에 보여질 게시글 개수

	public String getBoardList(SearchDto sDto, HttpSession session, Model model) {
		log.info("getBoardList");
		String view = "boardList";

		// DB에서 게시글 목록 가져오기
		int num = sDto.getPageNum();

		if (sDto.getListCnt() == 0) {
			sDto.setListCnt(lCnt); // 목록 개수 값 설정(초기 값 10개)
		}

		sDto.setPageNum((num - 1) * sDto.getListCnt());
		List<BoardDto> bList = bDao.selectBoardList(sDto);
		model.addAttribute("bList", bList);

		// 페이징 처리
		sDto.setPageNum(num);
		String pageHTML = getPaging(sDto);
		model.addAttribute("paging", pageHTML);

		// 페이지 관련 내용 세션에 저장
		if (sDto.getColname() != null) {
			session.setAttribute("sDto", sDto);
		} else {
			session.removeAttribute("sDto");
		}

		// 별개로 페이지 번호도 저장
		session.setAttribute("pageNum", num);

		return view;
	}

	private String getPaging(SearchDto sDto) {
		log.info("getPaging");
		String pageHTML = null;

		// 전체 게시글 개수
		int maxNum = bDao.selectBoardCnt(sDto);

		int pageCnt = 3; // 페이지에서 보여질 페이지 번호 개수

		String listName = "boardList?";

		if (sDto.getColname() != null) {
			// 검색 기능을 사용한 경우
			listName += "colname=" + sDto.getColname() + "&keyword=" + sDto.getKeyword() + "&";
		}

		PagingUtil paging = new PagingUtil(maxNum, sDto.getPageNum(), sDto.getListCnt(), pageCnt, listName);

		pageHTML = paging.makePaging();

		return pageHTML;
	}

	// 게시글, 파일 저장 및 회원 정보(point) 변경
	public String boardWrite(List<MultipartFile> files, BoardDto boardDto, HttpSession session,
			RedirectAttributes rttr) {
		log.info("boardWrite()");

		// 트랜젝션 상태 처리 객체
		TransactionStatus status = manager.getTransaction(definition);

		String view = null;
		String msg = null;

		try {
			// 게시글 저장
			bDao.insertBoard(boardDto);
			log.info("b_num:{}", boardDto.getB_num());

			// 파일 저장
			if (!files.get(0).isEmpty()) { // 업로드 파일이 있다면
				fileUpload(files, session, boardDto.getB_num());
			}
			// 작성자의 point 수정
			MemberDto member = (MemberDto) session.getAttribute("member");
			int point = member.getM_point() + 5;
			if (point > 100) {
				point = 100;
			}

			member.setM_point(point);
			mDao.updateMemberPoint(member);

			// 세션에 새 정보를 저장.
			member = mDao.selectMember(member.getM_id());
			session.setAttribute("member", member);

			// commit 수행
			manager.commit(status);
			view = "redirect:boardList?pageNum=1";
			msg = "작성 성공";

		} catch (Exception e) {
			e.printStackTrace();
			// rollback 수행
			manager.rollback(status);
			view = "redirect:writeForm";
			msg = "작성 실패";
		}

		rttr.addFlashAttribute("msg", msg);
		return view;
	}

	private void fileUpload(List<MultipartFile> files, HttpSession session, int b_num) throws Exception {
		// 파일 저장 실패 시 데이터베이스 롤백작업이 이루어지도록 예외를 throws 할 것.
		log.info("fileUpload()");

		// 파일 저장 위치 처리(session에서 저장 경로를 구함)
		String realPath = session.getServletContext().getRealPath("/");
		log.info("realPath : {}", realPath);

		realPath += "upload/"; // 파일 업로드용 폴더

		File folder = new File(realPath);
		if (folder.isDirectory() == false) {
			folder.mkdir(); // 폴더 생성
		}

		for (MultipartFile mf : files) {
			// 파일명 추출
			String oriname = mf.getOriginalFilename();

			BoardFileDto boardFileDto = new BoardFileDto();
			boardFileDto.setBf_oriname(oriname);
			boardFileDto.setBf_bnum(b_num);
			String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));

			boardFileDto.setBf_sysname(sysname);

			// 파일 저장
			File file = new File(realPath + sysname);
			mf.transferTo(file);

			// 파일 정보 저장
			bDao.insertFile(boardFileDto);
		}
	}

	public String getBoard(int b_num, HttpSession session, Model model) {
		log.info("getBoard()");

		// 게시글 번호(b_num)로 게시물 가져오기
		BoardDto boardDto = bDao.selectBoard(b_num);
		MemberDto member = (MemberDto) session.getAttribute("member");
		String mid = member.getM_id();
		
		// 조회수 증가 부분
		if (!mid.equals(boardDto.getB_id())) {
			int b_views = boardDto.getB_views() + 1;
			boardDto.setB_views(b_views);
			bDao.updateViews(boardDto);
		}

		model.addAttribute("board", boardDto);

		// 파일 목록 가져오기
		List<BoardFileDto> bfList = bDao.selectFileList(b_num);
		model.addAttribute("bfList", bfList);

		// 댓글 목록 가져오기
		List<ReplyDto> rList = bDao.selectReplList(b_num);
		model.addAttribute("rList", rList);

		return "boardDetail";
	}

	public ReplyDto replyInsert(ReplyDto reply) {

		log.info("replyInsert()");

		TransactionStatus status = manager.getTransaction(definition);

		try {
			bDao.insertReply(reply);
			reply = bDao.selectReply(reply.getR_num());

			manager.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			manager.rollback(status);
			reply = null;
		}
		return reply;
	}

	public ResponseEntity<Resource> fildDownload(BoardFileDto bfile, HttpSession session) throws IOException {
		log.info("fileDownload()");

		String realPath = session.getServletContext().getRealPath("/");
		realPath += "upload/" + bfile.getBf_sysname();

		// 실제 하드디스크에 저장된 파일과 연결하는 객체를 생성.
		InputStreamResource fResource = new InputStreamResource(new FileInputStream(realPath));

		// 파일명이 한글일 경우 인코딩 처리
		String fileName = URLEncoder.encode(bfile.getBf_oriname(), "UTF-8");

		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.cacheControl(CacheControl.noCache())
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ fileName)
				.body(fResource);
	}
}

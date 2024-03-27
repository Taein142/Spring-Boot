package com.icia.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.icia.board.dto.BoardDto;
import com.icia.board.dto.BoardFileDto;
import com.icia.board.dto.ReplyDto;
import com.icia.board.dto.SearchDto;

@Mapper
public interface BoardDao {
	//게시글 목록 가져오는 메소드
	List<BoardDto> selectBoardList(SearchDto sdto);
	//전체 게시글 개수 구하는 메소드
	int selectBoardCnt(SearchDto sdto);
	//게시글 내용 저장 메소드
	void insertBoard(BoardDto board);
	//파일 정보 저장 메소드
	void insertFile(BoardFileDto bFile);
	//게시글 가져오는 메소드
	BoardDto selectBoard(int b_num);
	//파일 목록 가져오는 메소드
	List<BoardFileDto> selectFileList(int b_num);
	//댓글 목록 가져오는 메소드
	List<ReplyDto> selectReplyList(int b_num);
	//댓글 저장 메소드
	void insertReply(ReplyDto reply);
	//마지막 저장 댓글 불러오기 메소드
	ReplyDto selectReply(int r_num);
	//파일 정보 삭제 메소드
	void deleteFile(String bf_sysname);
	//게시글 수정 메소드
	void updateBoard(BoardDto board);
	// 조회수 업데이트 메소드
	void updateViews(BoardDto boardDto);
	// 저장된 파일명 목록을 가져오는 메소드
	List<String> selectFnameList(int b_num);
	// 파일 목록 삭제 메소드
	void deleteFiles(int b_num);
	// 댓글 목록 삭제 메소드
	void deleteReplies(int b_num);
	// 게시글 삭제 메소드
	void deleteBoard(int b_num);
}
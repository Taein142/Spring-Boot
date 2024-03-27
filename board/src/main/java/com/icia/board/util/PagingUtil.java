package com.icia.board.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PagingUtil {
	private int maxNum;//전체 게시글 개수
	private int pageNum;//현재 보이게 되는 페이지의 번호
	private int listCnt;//페이지에 보여질 목록 개수
	private int pageCnt;//보여질 페이지 번호의 개수
	private String listName;//게시판이 여러개인 경우 url을 저장
	
	public String makePaging() {
		String page = null;
		StringBuffer sb = new StringBuffer();
		
		int totalPage = (maxNum % listCnt) > 0 ?
				(maxNum / listCnt) + 1 : maxNum / listCnt;
		
		int curGroup = (pageNum % pageCnt) > 0 ?
				(pageNum / pageCnt) + 1 : pageNum / pageCnt;
		
		int start = (curGroup * pageCnt) - (pageCnt - 1);
		int end = (curGroup * pageCnt) >= totalPage ?
				totalPage : curGroup * pageCnt;
		
		if(start != 1) {
			sb.append("<a class='pno' href='/" + listName 
					+ "pageNum=" + (start - 1) + "'>◀</a>");
		}//<a class='pno' href='/boardList?pageNum=5'>◀</a>
		
		for(int i = start; i <= end; i++) {
			if(i != pageNum) {
				sb.append("<a class='pno' href='/" + listName 
					+ "pageNum=" + i + "'>" + i + "</a>");
			} else {
				sb.append("<font class='pno'>" + i + "</font>");
			}
		}
		
		if(end != totalPage) {
			sb.append("<a class='pno' href='/" + listName 
					+ "pageNum=" + (end + 1) + "'>▶</a>");
		}
		
		page = sb.toString();
			
		return page;
	}
}




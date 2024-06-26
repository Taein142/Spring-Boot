package com.icia.thymeleafprj01.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.icia.thymeleafprj01.dto.ProductDTO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	@GetMapping("/")
	public String home(Model model) {
		log.info("home()");
		
		model.addAttribute("id","user");

		return "index";
	}
	
	@GetMapping("/second")
	public String second(Model model) {
		log.info("second()");
		
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
		String date = sdf.format(now);
		String date2 = "<h3>" + sdf.format(now) + "</h3>";
		
		model.addAttribute("date", date);
		model.addAttribute("date2", date2);
		
		// Map을 사용한 전송
		Map<String, String> rmap = new HashMap<>();
		rmap.put("pname", "치약");
		rmap.put("brand", "LG화학");
		rmap.put("price", "3000원");
		model.addAttribute("product", rmap);
		
		return "second";
	}
	
	@GetMapping("/third")
	public ModelAndView thrid() {
		log.info("third()");
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("third");
		
		//DTO에 데이터를 담아보자
		ProductDTO pDto1 = new ProductDTO();
		pDto1.setPname("세탁기");
		pDto1.setBrand("삼성");
		pDto1.setPrice(450000);
		pDto1.setAmount(10);
		
		mv.addObject("prod1", pDto1);
		
		ProductDTO pDto2 = new ProductDTO();
		pDto2.setPname("냉장고");
		pDto2.setBrand("삼성");
		pDto2.setPrice(800000);
		pDto2.setAmount(15);
		
		mv.addObject("prod1", pDto1);
		
		mv.addObject("prod2", pDto2);
		
		// 자바스크립트 영역으로 넘기는 데이터
		mv.addObject("msg", "경고장에 출력할 내용");
		
		return mv;
	}	
}

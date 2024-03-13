package com.icia.thymeleafprj02.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.icia.thymeleafprj02.dto.ProductDto;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {
	@GetMapping("/")
	public String home(Model model) {
		log.info("home()");
		
		model.addAttribute("date", new Date());
		
		return "index";
	}
	
	@GetMapping("second")
	public String second(@RequestParam("data1") String data1,
						 @RequestParam("data2") int data2) {
		log.info("second - {}, {}", data1, data2);
		
		return "inputForm";
	}
	
	@GetMapping("datain")
	public String datain(ProductDto product, Model model) {
		log.info("datain() - {}", product);
		model.addAttribute("product", product);
		return "updateForm";
	}
	
	@PostMapping("dataUpdate")
	public String dataUpdate(ProductDto product) {
		log.info("dataUpdate - {}", product);
		return "redirect:/";
	}
	
	@GetMapping("third")
	public String third(Model model) {
		log.info("third()");
		
		model.addAttribute("mid", "user");//로그인을 했다 치고...
		
		model.addAttribute("age", 37);
		
		//List 전송
		List<String> cafeMenus = new ArrayList<>();
		cafeMenus.add("아메리카노");
		cafeMenus.add("카페라떼");
		cafeMenus.add("카페모카");
		cafeMenus.add("에스프레소");
		cafeMenus.add("녹차");
		
		model.addAttribute("menus", cafeMenus);
		
		return "third";
	}
}






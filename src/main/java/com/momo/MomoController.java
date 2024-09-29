package com.momo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MomoController {

	@GetMapping("/")
	public String root() {
		return "redirect:/member/welcome";
	}
	
	
	@GetMapping("/about")
	public String about() {
		return "momo_about";
	}
	
}

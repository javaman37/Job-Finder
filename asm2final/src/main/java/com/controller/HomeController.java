package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RequestMapping("/")
public class HomeController {
	
	@GetMapping("/")
	public String showHome() {

		return "home";
	}
	@GetMapping("/loginAction")
	public String showLoginPage() {
		return "login";
	}
}

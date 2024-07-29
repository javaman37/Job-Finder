package com.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.entity.User;
import com.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private UserService userService; // Sử dụng UserService
	
	@GetMapping("login")
	public String showLoginPage() {
		return "login";
	}
	@PostMapping("logout")
	public String logOut() {
		return "home";
	}
	
	@PostMapping("/register")
	public String registerUserAccount(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
	    User existing = userService.findByEmail(user.getEmail());
	    if (existing != null) {
	        result.rejectValue("email", null, "There is already an account registered with that email");
	        model.addAttribute("msg_register_error", "There is already an account registered with that email");
	        return "redirect:/auth/login?register_error";
	    }

	    if (result.hasErrors()) {
	        model.addAttribute("msg_register_error", "Registration failed. Please correct the errors and try again.");
	        return "redirect:/auth/login?register_error";
	    }

	    userService.save(user);
	    model.addAttribute("msg_register_success", "Registration successful! Please log in.");
	    return "redirect:/auth/login?register_success";  // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
	}

	

}

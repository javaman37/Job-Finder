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
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.User;
import com.request.UserRequest;
import com.service.UserService;
import com.service.VerificationTokenService;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private VerificationTokenService tokenService; // Dịch vụ xác thực token
	
	@Autowired
    private UserService userService; // Dịch vụ quản lý người dùng

	// Hiển thị trang đăng nhập
	@GetMapping("login")
	public String showLoginPage() {
		return "login"; 
	}
	
	// Xử lý đăng xuất, điều hướng về trang chủ
	@PostMapping("logout")
	public String logOut() {
		return "home"; 
	}
	
	// Xử lý đăng ký tài khoản mới
	@PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("userRequest") @Valid UserRequest userRequest, 
                                      BindingResult result, 
                                      Model model) {
        if (result.hasErrors()) {
            model.addAttribute("msg_register_error", "Registration failed. Please correct the errors and try again.");
            return "redirect:/auth/login?register_error"; // Đăng ký lỗi, trả về trang đăng nhập
        }

        User existing = userService.findUserByEmail(userRequest.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
            model.addAttribute("msg_register_error", "There is already an account registered with that email");
            return "redirect:/auth/login?register_error"; // Email đã tồn tại
        }

        userService.registerUser(userRequest); // Đăng ký người dùng mới

        model.addAttribute("msg_register_success", "Registration successful! Please log in.");
        return "redirect:/auth/login?register_success"; // Đăng ký thành công
    }
	
	// Xác nhận tài khoản người dùng thông qua token
	@GetMapping("/confirm")
    public String confirmUser(@RequestParam("token") String token) {
        String result = tokenService.validateVerificationToken(token);
        if (result.equals("valid")) {
            User user = tokenService.getUserFromToken(token);
            userService.activateUser(user); // Kích hoạt tài khoản
            return "redirect:/auth/login?register_success"; // Token hợp lệ
        } else {
            return "Invalid or expired token"; // Token không hợp lệ hoặc hết hạn
        }
    }

}

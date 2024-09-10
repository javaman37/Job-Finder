package com.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dao.FeaturedCategory;
import com.dao.FeaturedCompany;
import com.dao.FeaturedJob;
import com.entity.User;
import com.service.CategoryService;
import com.service.CompanyService;
import com.service.RecruitmentService;
import com.service.UserService;

@Controller

public class HomeController {
	
	@Autowired
    private CompanyService companyService;
	@Autowired
    private UserService userService;
	
	@Autowired
    private RecruitmentService recruitmentService;
	
	@Autowired
    private CategoryService categoryService;
	
	@GetMapping("/")
	public String showHome(Model model,@AuthenticationPrincipal UserDetails userDetails,HttpSession session) {
		
		 if (userDetails != null) {
	            // Người dùng đã đăng nhập
	            User user = userService.findUserByEmail(userDetails.getUsername());
	            model.addAttribute("user_inpage", user);
	            session.setAttribute("user", user);  // Gán đối tượng user vào session
	        }
		
		 List<FeaturedCompany> topCompanies = companyService.getTopCompaniesByTotalApplications();
	        model.addAttribute("topCompanies", topCompanies);
	        
	     List<FeaturedJob> featuredJobs = recruitmentService.getTopFeaturedJobs();
	        model.addAttribute("featuredJobs", featuredJobs);
	        
	     List<FeaturedCategory> topCategories = categoryService.getTop4Categories();
	        model.addAttribute("topCategories", topCategories);
	        
	     return "home";
	}
}

package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dao.FeaturedCategory;
import com.dao.FeaturedCompany;
import com.dao.FeaturedJob;
import com.service.CategoryService;
import com.service.CompanyService;
import com.service.RecruitmentService;

@Controller
//@RequestMapping("/")
public class HomeController {
	
	@Autowired
    private CompanyService companyService;
	
	@Autowired
    private RecruitmentService recruitmentService;
	
	@Autowired
    private CategoryService categoryService;
	
	@GetMapping("/")
	public String showHome(Model model) {
		 List<FeaturedCompany> topCompanies = companyService.getTopCompaniesByTotalApplications();
	        model.addAttribute("topCompanies", topCompanies);
	        
	     List<FeaturedJob> featuredJobs = recruitmentService.getTopFeaturedJobs();
	        model.addAttribute("featuredJobs", featuredJobs);
	        
	     List<FeaturedCategory> topCategories = categoryService.getTop4Categories();
	        model.addAttribute("topCategories", topCategories);
	        
	        return "home";

		
	}

	
}

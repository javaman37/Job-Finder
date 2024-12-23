package com.service;

import org.springframework.data.domain.Page;

import com.entity.Company;
import com.entity.FollowCompany;
import com.entity.User;

public interface FollowCompanyService {
	
	boolean followCompany(User user, Company company); // Method to follow the company

	Page<FollowCompany> getFollowedCompaniesByUser(Long id, int page, int size);

	void deleteFollowCompany(Integer id) ;

	
}

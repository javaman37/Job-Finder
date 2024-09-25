package com.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.entity.Company;
import com.entity.FollowCompany;
import com.entity.User;

@Repository
public interface FollowCompanyDAO extends JpaRepository<FollowCompany, Integer> {

	boolean existsByUserAndCompany(User user, Company company); // Check if a user already follows a company
    
	
	@Query("SELECT f FROM FollowCompany f WHERE f.user.id = ?1")
	Page<FollowCompany> findByUserId( Long id, PageRequest pageable);


	void deleteById(Integer id);

}

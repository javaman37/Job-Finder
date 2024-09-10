package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.User;
import com.entity.VerificationToken;

@Repository
public interface VerificationTokenDAO extends JpaRepository<VerificationToken, Integer> {
	
	VerificationToken findByToken(String token);

	void deleteByUser(User user);


}


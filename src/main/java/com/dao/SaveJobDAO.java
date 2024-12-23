package com.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Recruitment;
import com.entity.SaveJob;
import com.entity.User;

@Repository
public interface SaveJobDAO extends JpaRepository<SaveJob, Integer> {

	SaveJob findByUserAndRecruitment(User user, Recruitment recruitment);
    
	
	Page<SaveJob> findByUser(User user, Pageable pageable);
	
	void deleteById(Long id);

}

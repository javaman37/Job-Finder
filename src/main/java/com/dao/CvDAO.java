package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Cv;

@Repository
public interface CvDAO extends JpaRepository<Cv, Long> {

	Cv findByUserId(Long userId);


}

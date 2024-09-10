package com.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.ApplyPost;

@Repository
public interface ApplyPostDAO extends JpaRepository<ApplyPost, Integer> {

	ApplyPost findByUserId(Long idUser);

	List<ApplyPost> findByRecruitmentId(Long id);

	
	// Phương thức tự động tạo truy vấn theo userId và phân trang
    Page<ApplyPost> findByUserId(Long userId, Pageable pageable);
	

}

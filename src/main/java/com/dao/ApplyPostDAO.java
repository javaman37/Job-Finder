package com.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.ApplyPost;
import com.entity.Recruitment;
import com.entity.User;

@Repository
public interface ApplyPostDAO extends JpaRepository<ApplyPost, Integer> {

	ApplyPost findByUserId(Long idUser);

	List<ApplyPost> findByRecruitmentId(Long id);
	
	// Phương thức tìm kiếm ApplyPost theo user và recruitment
    @Query("SELECT a FROM ApplyPost a WHERE a.user = :user AND a.recruitment = :recruitment")
    ApplyPost findByUserAndRecruitment(@Param("user") User user, @Param("recruitment") Recruitment recruitment);

	
	// Phương thức tự động tạo truy vấn theo userId và phân trang
    Page<ApplyPost> findByUserId(Long userId, Pageable pageable);
	

}

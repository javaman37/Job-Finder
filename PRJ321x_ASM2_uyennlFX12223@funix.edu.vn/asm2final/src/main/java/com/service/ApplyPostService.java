package com.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.entity.ApplyPost;
import com.entity.Recruitment;
import com.entity.User;

public interface ApplyPostService {

	void saveApplyPost(ApplyPost applyPost);

	String applyWithExistingCv(Long id, Long id2, String introduction);

	List<ApplyPost> findByRecruitmentId(Long id);

	Page<ApplyPost> getAppliedJobsByUser(Long id, int page, int size);

	ApplyPost findByUserAndRecruitment(User user, Recruitment recruitment);
	

}

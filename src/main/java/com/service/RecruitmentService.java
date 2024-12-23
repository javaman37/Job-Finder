package com.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dao.FeaturedJob;
import com.dto.ResultSearchDTO;
import com.entity.Recruitment;

public interface RecruitmentService {

	List<FeaturedJob> getTopFeaturedJobs();

	Page<Recruitment> getRecruitmentsByCompany(Integer companyId, Pageable pageable);

	Recruitment getRecruitmentById(Long id);

	void deleteRecruitment(Long id);

	void saveRecruitment(Recruitment recruitment);



	public ResultSearchDTO searchByTitle(String keySearch, Pageable pageable);

	public ResultSearchDTO searchByCompanyName(String keySearch, Pageable pageable);
	public ResultSearchDTO searchByAddress(String keySearch, Pageable pageable);



	



}

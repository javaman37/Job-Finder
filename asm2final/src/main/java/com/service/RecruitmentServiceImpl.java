package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dao.FeaturedJob;
import com.dao.RecruitmentDAO;
import com.dto.ResultSearchDTO;
import com.entity.Recruitment;
@Service
public class RecruitmentServiceImpl implements RecruitmentService {
	
	   @Autowired
	    private RecruitmentDAO recruitmentDAO;

	    @Override
	    public List<FeaturedJob> getTopFeaturedJobs() {
	        return recruitmentDAO.findTopFeaturedJobs(PageRequest.of(0, 5));
	    }

	    @Override
	    public Page<Recruitment> getRecruitmentsByCompany(Integer companyId, Pageable pageable) {
	        return recruitmentDAO.findByCompanyId(companyId, pageable);
	    }
	    
	    @Override
	    public Recruitment getRecruitmentById(Long id) {
	        return recruitmentDAO.findById(id).orElse(null);
	    }

	    @Override
	    public void saveRecruitment(Recruitment recruitment) {
	        recruitmentDAO.save(recruitment);
	    }

	    @Override
	    public void deleteRecruitment(Long id) {
	        recruitmentDAO.deleteById(id);
	    }

	    public ResultSearchDTO searchByTitle(String keySearch, Pageable pageable) {
	        Page<Recruitment> resultPage = recruitmentDAO.searchByTitle(keySearch, pageable);
	        
	        ResultSearchDTO dto = new ResultSearchDTO();
	        dto.setTotalPages(resultPage.getTotalPages());
	        dto.setContent(resultPage.getContent());
	        dto.setNumber(resultPage.getNumber());
	        return dto;
	    }
	    
	    public ResultSearchDTO searchByCompanyName(String keySearch, Pageable pageable) {
	        Page<Recruitment> resultPage = recruitmentDAO.searchByCompanyName(keySearch, pageable);
	        
	        ResultSearchDTO dto = new ResultSearchDTO();
	        dto.setTotalPages(resultPage.getTotalPages());
	        dto.setContent(resultPage.getContent());
	        dto.setNumber(resultPage.getNumber());
	        return dto;
	    }
	    
	    public ResultSearchDTO searchByAddress(String keySearch, Pageable pageable) {
	        Page<Recruitment> resultPage = recruitmentDAO.searchByAddress(keySearch, pageable);
	        
	        ResultSearchDTO dto = new ResultSearchDTO();
	        dto.setTotalPages(resultPage.getTotalPages());
	        dto.setContent(resultPage.getContent());
	        dto.setNumber(resultPage.getNumber());
	        return dto;
	    }
}

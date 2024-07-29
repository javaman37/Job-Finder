package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dao.FeaturedJob;
import com.dao.RecruitmentDAO;
@Service
public class RecruitmentServiceImpl implements RecruitmentService {
	
	   @Autowired
	    private RecruitmentDAO recruitmentDAO;

	    @Override
	    public List<FeaturedJob> getTopFeaturedJobs() {
	        return recruitmentDAO.findTopFeaturedJobs(PageRequest.of(0, 2));
	    }

}

package com.service;

import java.util.List;

import com.dao.FeaturedCompany;

public interface CompanyService {
	List<FeaturedCompany> getTopCompaniesByTotalApplications();

}

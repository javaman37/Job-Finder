package com.service;

import java.util.List;

import com.dao.FeaturedCompany;
import com.entity.Company;

public interface CompanyService {
	List<FeaturedCompany> getTopCompaniesByTotalApplications();
	Company getCompanyById(int id);
	//Company findById(Integer companyId);

}

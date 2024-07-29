package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dao.FeaturedCompany;
import com.dao.CompanyDAO;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDAO companyDAO;

    @Override
    public List<FeaturedCompany> getTopCompaniesByTotalApplications() {
        return companyDAO.findTopCompaniesByTotalApplications(PageRequest.of(0, 2));
    }
}

package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dao.FeaturedCompany;
import com.entity.Company;
import com.dao.CompanyDAO;

import java.util.List;

import javax.persistence.EntityNotFoundException;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDAO companyDAO;

    // Lấy danh sách các công ty hàng đầu dựa trên tổng số đơn ứng tuyển
    @Override
    public List<FeaturedCompany> getTopCompaniesByTotalApplications() {
        return companyDAO.findTopCompaniesByTotalApplications(PageRequest.of(0, 2));
    }
    
    // Lấy thông tin công ty theo ID
    @Override
    public Company getCompanyById(int id) {
        Company company = companyDAO.findById(id);
        if (company == null) {
            throw new EntityNotFoundException("Company not found with id: " + id); // Xử lý khi không tìm thấy công ty
        }
        return company;
    }
}

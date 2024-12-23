package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dao.FollowCompanyDAO;
import com.entity.Company;
import com.entity.FollowCompany;
import com.entity.User;

@Service
public class FollowCompanyServiceImpl implements FollowCompanyService {

    @Autowired
    private FollowCompanyDAO followCompanyDAO;

    // Xử lý việc theo dõi một công ty, trả về false nếu đã theo dõi trước đó
    @Override
    public boolean followCompany(User user, Company company) {
        if (followCompanyDAO.existsByUserAndCompany(user, company)) {
            return false; // Người dùng đã theo dõi công ty này
        }

        // Lưu mối quan hệ theo dõi mới
        FollowCompany follow = new FollowCompany();
        follow.setUser(user);
        follow.setCompany(company);
        followCompanyDAO.save(follow);
        return true;
    }

    // Lấy danh sách công ty đã theo dõi của người dùng với phân trang
    @Override
    public Page<FollowCompany> getFollowedCompaniesByUser(Long id, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return followCompanyDAO.findByUserId(id, pageable);
    }

    // Xóa theo dõi công ty theo ID
    @Override
    public void deleteFollowCompany(Integer id) {
        followCompanyDAO.deleteById(id);
    }
}

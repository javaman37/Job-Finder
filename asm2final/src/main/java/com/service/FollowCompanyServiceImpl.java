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
	

	@Override
    public boolean followCompany(User user, Company company) {
        if (followCompanyDAO.existsByUserAndCompany(user, company)) {
            return false; // User already follows the company
        }

        // Save the new follow relationship
        FollowCompany follow = new FollowCompany();
        follow.setUser(user);
        follow.setCompany(company);
        followCompanyDAO.save(follow);
        return true;
    }
	
	
	
	@Override
	public Page<FollowCompany> getFollowedCompaniesByUser(Long id, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
        return followCompanyDAO.findByUserId(id, pageable);
	}



	@Override
	public void deleteFollowCompany(Integer id) {
		followCompanyDAO.deleteById(id);
		
	}


	

}

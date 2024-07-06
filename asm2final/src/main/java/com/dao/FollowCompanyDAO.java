package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.FollowCompany;

@Repository
public interface FollowCompanyDAO extends JpaRepository<FollowCompany, Integer> {

}

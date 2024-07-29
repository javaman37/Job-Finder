package com.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.entity.Company;

import java.util.List;

@Repository
public interface CompanyDAO extends CrudRepository<Company, Long> {

    @Query("SELECT c.id AS id, c.nameCompany AS nameCompany, COUNT(a.id) AS totalApplications " +
           "FROM Company c " +
           "JOIN c.recruitments r " +
           "JOIN r.applyPosts a " +
           "GROUP BY c.id, c.nameCompany " +
           "ORDER BY COUNT(a.id) DESC")
    List<FeaturedCompany> findTopCompaniesByTotalApplications(Pageable pageable);
}

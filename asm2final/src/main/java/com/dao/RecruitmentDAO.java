package com.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.entity.Recruitment;

import java.util.List;

@Repository
public interface RecruitmentDAO extends CrudRepository<Recruitment, Long> {

	 @Query("SELECT r.id AS id, r.title AS title, r.salary AS salary, r.address AS address, COUNT(a.id) AS totalApplications, r.type AS type, r.quantity AS quantity, c.nameCompany AS nameCompany " +
	           "FROM Recruitment r " +
	           "JOIN r.applyPosts a " +
	           "JOIN r.company c " +
	           "GROUP BY r.id, r.title, r.salary, r.address, r.type, r.quantity, c.nameCompany " +
	           "ORDER BY COUNT(a.id) DESC, r.salary DESC, r.quantity DESC")
    List<FeaturedJob> findTopFeaturedJobs(Pageable pageable);
}


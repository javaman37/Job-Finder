package com.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.Recruitment;

import java.util.List;

@Repository
public interface RecruitmentDAO extends CrudRepository<Recruitment, Long> {

	@Query("SELECT r.id AS id, r.title AS title, r.salary AS salary, r.address AS address, COUNT(a.id) AS totalApplications, r.type AS type, r.quantity AS quantity, c.nameCompany AS nameCompany "
			+ "FROM Recruitment r " + "JOIN r.applyPosts a " + "JOIN r.company c "
			+ "GROUP BY r.id, r.title, r.salary, r.address, r.type, r.quantity, c.nameCompany "
			+ "ORDER BY COUNT(a.id) DESC, r.salary DESC, r.quantity DESC")
	List<FeaturedJob> findTopFeaturedJobs(Pageable pageable);


	
	//Page<Recruitment> findAll(Pageable pageable);

	@Query("SELECT r FROM Recruitment r WHERE r.title LIKE %:keySearch%")
	Page<Recruitment> searchByTitle(@Param("keySearch") String keySearch,Pageable pageable);
	
	@Query("SELECT r FROM Recruitment r WHERE r.company.nameCompany LIKE %:keySearch%")
    Page<Recruitment> searchByCompanyName(@Param("keySearch") String keySearch, Pageable pageable);

    @Query("SELECT r FROM Recruitment r WHERE r.address LIKE %:keySearch%")
    Page<Recruitment> searchByAddress(@Param("keySearch") String keySearch, Pageable pageable);


    @Query("SELECT r FROM Recruitment r WHERE r.company.id = :companyId")
    Page<Recruitment> findByCompanyId(@Param("companyId") Integer companyId, Pageable pageable);

	

}

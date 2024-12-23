package com.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.entity.Category;

@Repository
public interface CategoryDAO extends CrudRepository<Category, Long> {
	   @Query("SELECT c.id AS id, c.name AS name, COUNT(r.id) AS totalJobs " +
	           "FROM Category c " +
	           "JOIN c.recruitments r " +
	           "GROUP BY c.id, c.name " +
	           "ORDER BY COUNT(r.id) DESC")
	    List<FeaturedCategory> findTop4Categories(Pageable pageable);

	   Category findCategoryById(Long categoryId);

}

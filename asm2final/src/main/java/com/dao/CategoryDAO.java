package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Category;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer> {

}

package com.service;

import java.util.List;

import com.dao.FeaturedCategory;
import com.entity.Category;

public interface CategoryService {
	List<FeaturedCategory> getTop4Categories();

	List<Category> getAllCategories();

	Category getCategoryById(Long categoryId);

	

}

package com.service;

import java.util.List;

import com.dao.FeaturedCategory;

public interface CategoryService {
	List<FeaturedCategory> getTop4Categories();

}

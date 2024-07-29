package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dao.CategoryDAO;
import com.dao.FeaturedCategory;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public List<FeaturedCategory> getTop4Categories() {
        return categoryDAO.findTop4Categories(PageRequest.of(0, 4));
    }
}

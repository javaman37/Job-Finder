package com.dao;

import com.entity.Company;

public interface FeaturedJob {
    Long getId();
    String getTitle();
    String getAddress();
    Integer getTotalApplications();
    String getType();
    Integer getQuantity();
    String getNameCompany();

}

package com.dao;

public interface CurrentJob {
	Long getId();
    String getTitle();
    String getAddress();
    Integer getTotalApplications();
    String getType();
    Integer getQuantity();
    String getNameCompany();

}

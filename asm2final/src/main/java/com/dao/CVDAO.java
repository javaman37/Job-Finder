package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.CV;

@Repository
public interface CVDAO extends JpaRepository<CV, Integer> {

}

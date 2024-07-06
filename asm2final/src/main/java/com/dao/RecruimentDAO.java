package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Recruiment;

@Repository
public interface RecruimentDAO extends JpaRepository<Recruiment, Integer> {

}

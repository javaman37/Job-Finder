package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.SaveJob;

@Repository
public interface SaveJobDAO extends JpaRepository<SaveJob, Integer> {

}

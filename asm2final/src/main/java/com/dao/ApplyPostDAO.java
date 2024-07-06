package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.ApplyPost;

@Repository
public interface ApplyPostDAO extends JpaRepository<ApplyPost, Integer> {

}

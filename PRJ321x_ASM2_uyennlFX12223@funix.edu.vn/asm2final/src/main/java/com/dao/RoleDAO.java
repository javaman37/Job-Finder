package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Role;

@Repository
public interface RoleDAO extends JpaRepository<Role, Integer> {

}

package com.service;

import com.entity.User;

public interface UserService {
	void save(User user);
    User findByEmail(String email);

}

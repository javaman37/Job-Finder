package com.service;

import org.springframework.web.multipart.MultipartFile;

import com.entity.Company;
import com.entity.User;
import com.request.UserRequest;

public interface UserService {
	
    void saveCompany(Company company);
	User findUserByEmail(String email);
	User findUserById(Long id);

	void createVerificationToken(User user, String token);
	void updateUserProfile(User user);
	void registerUser(UserRequest userRequest);
	void activateUser(User user);
	//void update(User user);
	boolean applyJob(MultipartFile file, Long id, String introduction, User user);
	User getCurrentUser();
	
}

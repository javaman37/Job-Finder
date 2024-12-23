package com.service;

import com.entity.VerificationToken;
import com.entity.User;

public interface VerificationTokenService {
    VerificationToken findByToken(String token);
    void createVerificationToken(User user, String token);
    void save(VerificationToken verificationToken);

    
	String validateVerificationToken(String token);
	User getUserFromToken(String token);
}

package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dao.VerificationTokenDAO;
import com.entity.VerificationToken;
import com.entity.User;
import java.util.Calendar;
import java.util.Date;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenDAO verificationTokenDAO;
    
    
    

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenDAO.findByToken(token);
    }
    

    @Override
    public void save(VerificationToken verificationToken) {
        verificationTokenDAO.save(verificationToken);
    }

    @Override
    public void createVerificationToken(User user, String token) {
    	 // Xóa token cũ
    	verificationTokenDAO.deleteByUser(user);
    	
    	
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(calculateExpiryDate(24 * 60)); // 24 hours expiry
        verificationTokenDAO.save(verificationToken);
    }
    

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
    
    
    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenDAO.findByToken(token);
        if (verificationToken == null) {
            return "invalid";
        }

        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "expired";
        }

        return "valid";
    }
    

    @Override
    public User getUserFromToken(String token) {
        VerificationToken verificationToken = verificationTokenDAO.findByToken(token);
        return verificationToken.getUser();
    }
}

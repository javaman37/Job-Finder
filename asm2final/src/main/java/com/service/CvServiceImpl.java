package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.CvDAO;
import com.dao.UserDAO;
import com.entity.Cv;
import com.entity.User;

@Service
public class CvServiceImpl implements CvService {
	@Autowired
    private CvDAO cvDAO; 
	@Autowired
    private UserDAO userDAO; 
	

	@Override
    public void saveCv(Cv cv) {
		
		Cv existingCv = cvDAO.findByUserId(cv.getUser().getId());
        if (existingCv != null) {
            existingCv.setFileName(cv.getFileName());
            cvDAO.save(existingCv);
        }
        cvDAO.save(cv);  // Sử dụng phương thức save mặc định
    }

	@Override
	public Cv findById(Long id) {
		return cvDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		Cv cv = cvDAO.findById(id).orElse(null);
		if(cv != null) {
			User u = cv.getUser();
			u.setCv(null);
			cvDAO.delete(cv);
			userDAO.save(u);
			
			
		}
		
	}

}

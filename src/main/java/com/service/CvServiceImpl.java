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

    // Lưu hoặc cập nhật thông tin CV
    @Override
    public void saveCv(Cv cv) {
        Cv existingCv = cvDAO.findByUserId(cv.getUser().getId());
        if (existingCv != null) {
            existingCv.setFileName(cv.getFileName()); // Cập nhật tên file CV nếu đã tồn tại
            cvDAO.save(existingCv);
        }
        cvDAO.save(cv);  // Lưu CV mới nếu chưa có
    }

    // Tìm kiếm CV theo ID
    @Override
    public Cv findById(Long id) {
        return cvDAO.findById(id).orElse(null); // Trả về CV nếu tìm thấy, ngược lại trả về null
    }

    // Xóa CV theo ID và cập nhật thông tin người dùng
    @Override
    @Transactional
    public void delete(Long id) {
        Cv cv = cvDAO.findById(id).orElse(null);
        if (cv != null) {
            User u = cv.getUser();
            u.setCv(null); // Xóa liên kết giữa User và CV
            cvDAO.delete(cv); // Xóa CV
            userDAO.save(u); // Cập nhật người dùng
        }
    }
}

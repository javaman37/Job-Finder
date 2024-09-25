package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.RecruitmentDAO;
import com.dao.SaveJobDAO;
import com.dao.UserDAO;
import com.entity.Recruitment;
import com.entity.SaveJob;
import com.entity.User;

@Service
public class SaveJobServiceImpl implements SaveJobService {
    
    @Autowired
    private SaveJobDAO saveJobDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RecruitmentDAO recruitmentDAO;
    
    // Lưu công việc vào danh sách yêu thích của người dùng
    @Override
    public boolean saveJob(Long userId, Long idRe) {
        User user = userDAO.findById(userId).orElse(null);
        Recruitment recruitment = recruitmentDAO.findById(idRe).orElse(null);

        if (user == null || recruitment == null) {
            return false; // Người dùng hoặc công việc không tồn tại
        }

        SaveJob existingSaveJob = saveJobDAO.findByUserAndRecruitment(user, recruitment);

        if (existingSaveJob != null) {
            return false; // Công việc đã được lưu trước đó
        }

        SaveJob saveJob = new SaveJob();
        saveJob.setUser(user);
        saveJob.setRecruitment(recruitment);
        saveJobDAO.save(saveJob);
        return true;
    }

    // Lấy danh sách công việc đã lưu của người dùng với phân trang
    @Override
    public Page<SaveJob> getSavedJobs(User user, int page) {
        Pageable pageable = PageRequest.of(page, 5); // Mỗi trang có tối đa 5 công việc
        return saveJobDAO.findByUser(user, pageable);
    }
    
    // Xóa công việc đã lưu theo ID
    @Override
    @Transactional
    public void deleteSaveJob(Long id) {
        saveJobDAO.deleteById(id);
    }
}

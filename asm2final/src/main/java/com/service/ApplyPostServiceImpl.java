package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.ApplyPostDAO;
import com.entity.ApplyPost;
import com.entity.Cv;
import com.entity.Recruitment;
import com.entity.User;

@Service
public class ApplyPostServiceImpl implements ApplyPostService {
	@Autowired
    private ApplyPostDAO applyPostDAO; 
	
	@Autowired
    private UserService userService;

    @Autowired
    private RecruitmentService recruitmentService;

	@Override
	public void saveApplyPost(ApplyPost applyPost) {
		applyPostDAO.save(applyPost);
		
	}

	@Override
	@Transactional
	public String applyWithExistingCv(Long idUser, Long idRe, String introduction) {
		
		try {
            Recruitment recruitment = recruitmentService.getRecruitmentById(idRe);
            User user = userService.findUserById(idUser);
            Cv cv = user.getCv();

            if (cv == null) {
                return "cv_required";  // Không có CV nào trong hồ sơ người dùng
            }

            // Kiểm tra nếu người dùng đã ứng tuyển trước đó
            ApplyPost existingApplyPost = applyPostDAO.findByUserId(idUser);
            ApplyPost applyPost;
            
            if (existingApplyPost != null) {
            	// Nếu đã ứng tuyển trước đó, cập nhật thông tin ứng tuyển
//                applyPost = existingApplyPost;
//                applyPost.setText(introduction);
//                applyPost.setNameCv(cv.getFileName());
            	return "error";
            } else {
            	
            
            	// Nếu chưa ứng tuyển, tạo mới
                applyPost = new ApplyPost();
                applyPost.setRecruitment(recruitment);
                applyPost.setUser(user);
                applyPost.setNameCv(cv.getFileName());
                applyPost.setText(introduction);
                applyPost.setStatus(1); // Status 1: Application submitted
            }

            applyPostDAO.save(applyPost);
            return "true";  // Ứng tuyển thành công

        } catch (Exception e) {
            e.printStackTrace();
            return "error";  // Xử lý lỗi chung
        }
    }

	@Override
	public List<ApplyPost> findByRecruitmentId(Long id) {
		 return applyPostDAO.findByRecruitmentId(id);
	}

	@Override
	public Page<ApplyPost> getAppliedJobsByUser(Long id, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
	    return applyPostDAO.findByUserId(id, pageable);
	}
}
	
	 

	



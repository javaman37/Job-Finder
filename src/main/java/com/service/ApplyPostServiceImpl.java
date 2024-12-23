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

	// Lưu thông tin ứng tuyển
	@Override
	public void saveApplyPost(ApplyPost applyPost) {
		applyPostDAO.save(applyPost);
	}

	// Xử lý ứng tuyển với CV đã có sẵn
	@Override
	@Transactional
	public String applyWithExistingCv(Long idUser, Long idRe, String introduction) {
		try {
            Recruitment recruitment = recruitmentService.getRecruitmentById(idRe);
            User user = userService.findUserById(idUser);
            Cv cv = user.getCv();

            if (cv == null) {
                return "cv_required";  // Không có CV trong hồ sơ người dùng
            }

            
            
         // Kiểm tra xem người dùng đã ứng tuyển công việc này chưa
            ApplyPost existingApplyPost = applyPostDAO.findByUserAndRecruitment(user, recruitment);
            ApplyPost applyPost; 
            
            if (existingApplyPost != null) {
            	// Nếu đã ứng tuyển trước đó, trả về lỗi
            	return "error";
            } else {
            	// Nếu chưa ứng tuyển, tạo mới đơn ứng tuyển
                applyPost = new ApplyPost();
                applyPost.setRecruitment(recruitment);
                applyPost.setUser(user);
                applyPost.setNameCv(cv.getFileName());
                applyPost.setText(introduction);
                applyPost.setStatus(1); // Status 1: Application submitted
            }

            applyPostDAO.save(applyPost);  // Lưu đơn ứng tuyển mới
            return "true";  // Ứng tuyển thành công

        } catch (Exception e) {
            e.printStackTrace();
            return "error";  // Xử lý lỗi chung
        }
    }
	
	
	@Override
    public ApplyPost findByUserAndRecruitment(User user, Recruitment recruitment) {
        return applyPostDAO.findByUserAndRecruitment(user, recruitment);
    }

	// Lấy danh sách ứng tuyển theo ID bài đăng tuyển dụng
	@Override
	public List<ApplyPost> findByRecruitmentId(Long id) {
		return applyPostDAO.findByRecruitmentId(id);
	}

	// Lấy danh sách các công việc đã ứng tuyển của người dùng theo trang
	@Override
	public Page<ApplyPost> getAppliedJobsByUser(Long id, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
	    return applyPostDAO.findByUserId(id, pageable);
	}
}

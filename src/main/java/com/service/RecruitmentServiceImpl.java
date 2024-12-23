package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dao.FeaturedJob;
import com.dao.RecruitmentDAO;
import com.dto.ResultSearchDTO;
import com.entity.Recruitment;

@Service
public class RecruitmentServiceImpl implements RecruitmentService {

    @Autowired
    private RecruitmentDAO recruitmentDAO;

    // Lấy danh sách công việc nổi bật với phân trang
    @Override
    public List<FeaturedJob> getTopFeaturedJobs() {
        return recruitmentDAO.findTopFeaturedJobs(PageRequest.of(0, 5));
    }

    // Lấy danh sách bài tuyển dụng theo công ty với phân trang
    @Override
    public Page<Recruitment> getRecruitmentsByCompany(Integer companyId, Pageable pageable) {
        return recruitmentDAO.findByCompanyId(companyId, pageable);
    }
    
    // Lấy thông tin bài tuyển dụng theo ID
    @Override
    public Recruitment getRecruitmentById(Long id) {
        return recruitmentDAO.findById(id).orElse(null);
    }

    // Lưu thông tin bài tuyển dụng mới hoặc cập nhật
    @Override
    public void saveRecruitment(Recruitment recruitment) {
        recruitmentDAO.save(recruitment);
    }

    // Xóa bài tuyển dụng theo ID
    @Override
    public void deleteRecruitment(Long id) {
        recruitmentDAO.deleteById(id);
    }

    // Tìm kiếm bài tuyển dụng theo tiêu đề
    public ResultSearchDTO searchByTitle(String keySearch, Pageable pageable) {
        Page<Recruitment> resultPage = recruitmentDAO.searchByTitle(keySearch, pageable);
        
        ResultSearchDTO dto = new ResultSearchDTO();
        dto.setTotalPages(resultPage.getTotalPages());
        dto.setContent(resultPage.getContent());
        dto.setNumber(resultPage.getNumber());
        return dto;
    }
    
    // Tìm kiếm bài tuyển dụng theo tên công ty
    public ResultSearchDTO searchByCompanyName(String keySearch, Pageable pageable) {
        Page<Recruitment> resultPage = recruitmentDAO.searchByCompanyName(keySearch, pageable);
        
        ResultSearchDTO dto = new ResultSearchDTO();
        dto.setTotalPages(resultPage.getTotalPages());
        dto.setContent(resultPage.getContent());
        dto.setNumber(resultPage.getNumber());
        return dto;
    }
    
    // Tìm kiếm bài tuyển dụng theo địa chỉ
    public ResultSearchDTO searchByAddress(String keySearch, Pageable pageable) {
        Page<Recruitment> resultPage = recruitmentDAO.searchByAddress(keySearch, pageable);
        
        ResultSearchDTO dto = new ResultSearchDTO();
        dto.setTotalPages(resultPage.getTotalPages());
        dto.setContent(resultPage.getContent());
        dto.setNumber(resultPage.getNumber());
        return dto;
    }
}

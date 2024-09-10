package com.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dao.CompanyDAO;
import com.dao.RoleDAO;
import com.dao.UserDAO;
import com.dao.VerificationTokenDAO;
import com.entity.ApplyPost;
import com.entity.Company;
import com.entity.Cv;
import com.entity.Recruitment;
import com.entity.Role;
import com.entity.User;
import com.entity.VerificationToken;
import com.request.UserRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CompanyDAO companyDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private VerificationTokenDAO verificationTokenDAO;

	@Autowired
	private EmailService emailService; // Sử dụng EmailService cho các thao tác liên quan đến email

	@Autowired
	private RecruitmentService recruitmentService;

	@Autowired
	private CvService cvService;

	@Autowired
	private ApplyPostService applyPostService;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public User findUserByEmail(String email) {
		return userDAO.findUserByEmail(email);
	}

	@Override
	public void saveCompany(Company company) {
		companyDAO.save(company);
	}

	@Override
	public User findUserById(Long id) {
		return userDAO.findById(id).orElse(null);
	}

	@Override
	public void registerUser(UserRequest userRequest) {

		User user = new User();

		user.setEmail(userRequest.getEmail());
		user.setFullName(userRequest.getFullName());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

		Role role = roleDAO.findById(userRequest.getRoleId()).orElse(null);
		user.setRole(role);

		userDAO.save(user);

		// Tạo token xác thực
		String token = UUID.randomUUID().toString();
		createVerificationToken(user, token);

		// Gửi email xác thực
		String recipientAddress = user.getEmail();
		String subject = "Xác Thực Đăng Ký";
		String confirmationUrl = "http://localhost:8080/asm2final/auth/confirm?token=" + token;
		String message = "Nhấp vào đường link để xác thực đăng ký của bạn: " + confirmationUrl;

		emailService.sendSimpleMessage(recipientAddress, subject, message);
	}

	@Override
	public void createVerificationToken(User user, String token) {
		// Tạo và lưu token xác thực (cần triển khai chi tiết)
		VerificationToken verificationToken = new VerificationToken(token, user);
		verificationTokenDAO.save(verificationToken);
	}

	@Override
	public void updateUserProfile(User user) {

		User existingUser = userDAO.findById(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getId()));

		existingUser.setAddress(user.getAddress());
		existingUser.setDescription(user.getDescription());
		existingUser.setEmail(user.getEmail());
		existingUser.setFullName(user.getFullName());
		existingUser.setPhoneNumber(user.getPhoneNumber());

		userDAO.save(existingUser);

	}

	@Override
	public void activateUser(User user) {
		user.setStatus(1); // 1 là trạng thái active
		userDAO.save(user);

	}

	@Override
	public boolean applyJob(MultipartFile file, Long id, String introduction, User user) {

		try {
			Recruitment recruitment = recruitmentService.getRecruitmentById(id);

			Cv cv = handleCv(file, user);  

			if (recruitment != null && user != null && cv != null) {
				ApplyPost applyPost = new ApplyPost();
				applyPost.setRecruitment(recruitment);
				applyPost.setUser(user);
				applyPost.setNameCv(cv.getFileName());
				applyPost.setText(introduction);
				applyPost.setStatus(1); // Status 1: Application submitted
				applyPostService.saveApplyPost(applyPost);
				return true; // Ứng tuyển thành công
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; // Xử lý lỗi chung
	}

	private Cv handleCv(MultipartFile file, User user) throws Exception {
		Cv cv = user.getCv(); // Lấy CV hiện tại của người dùng

		if (!file.isEmpty()) {
			// Save new CV
			String fileName = file.getOriginalFilename();
			String filePath = fileUploadService.saveCv(fileName, file);

			// Save CV in database
			if (cv == null) {
				cv = new Cv();
			}
			cv.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));
			cv.setUser(user);
			cvService.saveCv(cv);

			// Update user's CV
			user.setCv(cv);
			userService.updateUserProfile(user);
		} else {
			if (cv == null) {
				throw new Exception("CV is required");
			}
		}

		return cv;
	}

	@Override
	public User getCurrentUser() {
		// Lấy thông tin UserDetails từ Spring Security
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Tìm đối tượng User dựa trên email (hoặc username)
        return userDAO.findUserByEmail(userDetails.getUsername());
	}
}

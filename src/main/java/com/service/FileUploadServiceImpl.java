package com.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dao.CompanyDAO;
import com.dao.CvDAO;
import com.dao.UserDAO;
import com.entity.Company;
import com.entity.Cv;
import com.entity.User;

@Service
public class FileUploadServiceImpl implements FileUploadService {

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CompanyDAO companyDAO;
	
	@Autowired
	private CvDAO cvDAO;

	// Xử lý tải ảnh người dùng lên và cập nhật đường dẫn vào database
	@Override
	public void uploadUserImage(String email, String filePath) {
		User user = userDAO.findUserByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		user.setImage(filePath);
		userDAO.save(user);
	}

	// Xử lý tải logo công ty lên và cập nhật đường dẫn vào database
	@Override
	public void uploadCompanyLogo(String email, String filePath) {
		User user = userDAO.findUserByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found");
		}

		Company company = user.getCompany();
		if (company == null) {
			company = new Company();
			company.setUser(user);
			user.setCompany(company);
		}

		company.setLogo(filePath);
		companyDAO.save(company);
	}
	
	// Lưu file ảnh và trả về đường dẫn lưu trữ
	@Override
	public String saveFile(String fileName, MultipartFile multipartFile, String email) throws IOException {
		Path uploadPath = Paths.get(servletContext.getRealPath("/assets/images"));

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		String fileCode = FilenameUtils.getBaseName(fileName) + "-" + System.currentTimeMillis();

		try (var inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
			Files.copy(inputStream, filePath);
			return "/assets/images/" + fileCode + "-" + fileName;
		} catch (IOException ioe) {
			throw new IOException("Could not save file: " + fileName, ioe);
		}
	}
	
	// Xử lý tải CV của người dùng lên và cập nhật thông tin vào database
	@Transactional
	@Override
	public void uploadUserCv(String email, String filePath) {
		User user = userDAO.findUserByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found");
		}

		// Kiểm tra nếu user đã có CV, cập nhật hoặc tạo mới
	    Cv existingCv = user.getCv(); 

	    if (existingCv != null) {
	        existingCv.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));
	        cvDAO.save(existingCv);
	    } else {
	        Cv newCv = new Cv();
	        newCv.setUser(user);
	        newCv.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));

	        cvDAO.save(newCv);
	        user.setCv(newCv);
	        userDAO.save(user);
	    }
	}
	
	// Lưu file CV vào thư mục và trả về đường dẫn lưu trữ
	@Override
	public String saveCv(String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(servletContext.getRealPath("/assets/file-upload/"));

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		String fileCode = FilenameUtils.getBaseName(fileName) + "-" + System.currentTimeMillis();

		try (var inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileCode + "." + FilenameUtils.getExtension(fileName));
			Files.copy(inputStream, filePath);
			return "/assets/file-upload/" + fileCode + "." + FilenameUtils.getExtension(fileName);
		} catch (IOException ioe) {
			throw new IOException("Could not save file: " + fileName, ioe);
		}
	}

	// Lưu file CV theo email và trả về đường dẫn lưu trữ
	@Override
	public String saveCv(String fileName, MultipartFile multipartFile, String email) throws IOException {
		Path uploadPath = Paths.get(servletContext.getRealPath("/assets/file-upload/"));

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		String fileCode = FilenameUtils.getBaseName(fileName) + "-" + System.currentTimeMillis();

		try (var inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileCode + "." + FilenameUtils.getExtension(fileName));
			Files.copy(inputStream, filePath);
			return "/assets/file-upload/" + fileCode + "." + FilenameUtils.getExtension(fileName);
		} catch (IOException ioe) {
			throw new IOException("Could not save file: " + fileName, ioe);
		}
	}
}

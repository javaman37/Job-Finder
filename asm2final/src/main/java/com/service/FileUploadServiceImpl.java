package com.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

	

	

	@Override
	public void uploadUserImage(String email, String filePath) {
		User user = userDAO.findUserByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		user.setImage(filePath);
		userDAO.save(user);

	}

	

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
	
	
	
	//XỬ lý cv/file
	@Override
	public void uploadUserCv(String email, String filePath) {
		User user = userDAO.findUserByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found");
		}

		// Tạo một đối tượng Cv mới và thiết lập các giá trị
		Cv cv = new Cv();
		cv.setUser(user); // Liên kết CV với người dùng
		cv.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1)); // Lưu tên file từ đường dẫn

		// Lưu đối tượng Cv vào cơ sở dữ liệu
		cvDAO.save(cv);

		// Cập nhật User với đối tượng Cv mới
		user.setCv(cv); // Thiết lập Cv trong User
		userDAO.save(user); // Lưu User, Hibernate sẽ tự động cập nhật cv_id
	}
	
	

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

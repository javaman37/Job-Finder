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

	

	
// Xử lý hình ảnh
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
	@Transactional
	@Override
	public void uploadUserCv(String email, String filePath) {
		User user = userDAO.findUserByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found");
		}

		// Kiểm tra xem user đã có CV hay chưa
	    Cv existingCv = user.getCv(); // Sử dụng mối quan hệ giữa User và Cv

	    if (existingCv != null) {
	        // Nếu đã có CV, chỉ cần cập nhật tên file mới
	        existingCv.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));
	        cvDAO.save(existingCv); // Lưu thay đổi vào database
	    } else {
	        // Nếu chưa có CV, tạo mới và liên kết với user
	        Cv newCv = new Cv();
	    
	        
	        newCv.setUser(user); // Liên kết CV với người dùng
	        newCv.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1)); // Lưu tên file từ đường dẫn

	        cvDAO.save(newCv); // Lưu đối tượng CV vào cơ sở dữ liệu
	        
	        // Cập nhật user với đối tượng CV mới
	        user.setCv(newCv); // Thiết lập CV trong user
	        userDAO.save(user); // Lưu user, Hibernate sẽ tự động cập nhật cv_id

	        
	    }
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

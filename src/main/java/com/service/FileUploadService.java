package com.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
	
	
	void uploadUserImage(String email, String filePath);
	void uploadUserCv(String email, String filePath);
    void uploadCompanyLogo(String email, String filePath);
	String saveFile(String fileName, MultipartFile multipartFile, String email) throws IOException;
	String saveCv(String fileName, MultipartFile multipartFile, String email) throws IOException;
	String saveCv(String fileName, MultipartFile file)throws IOException;
	

}

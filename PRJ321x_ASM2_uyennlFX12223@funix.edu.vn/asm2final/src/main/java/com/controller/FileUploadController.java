package com.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.respone.FileUploadResponse;
import com.service.FileUploadService;

@RestController
public class FileUploadController {
	
	@Autowired
    private FileUploadService fileUploadService; // Dịch vụ xử lý tải tệp lên
	
	// Upload CV người dùng
	@PostMapping("/user/uploadCv")
    public ResponseEntity<FileUploadResponse> uploadCv(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("email") String email) throws IOException {
    	String filePath = fileUploadService.saveCv(file.getOriginalFilename(), file, email);
        fileUploadService.uploadUserCv(email, filePath); // Lưu và liên kết CV với người dùng

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(file.getOriginalFilename());
        response.setDownloadUri(filePath);
        response.setSize(file.getSize());

        return new ResponseEntity<>(response, HttpStatus.OK); // Trả về phản hồi thành công
    }
    
	// Upload ảnh người dùng
	@PostMapping("/upload-user-photo")
    public ResponseEntity<FileUploadResponse> uploadUserImage(@RequestParam("file") MultipartFile file,
                                                              @RequestParam("email") String email) throws IOException {
        String filePath = fileUploadService.saveFile(file.getOriginalFilename(), file, email);
        fileUploadService.uploadUserImage(email, filePath); // Lưu và liên kết ảnh với người dùng

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(file.getOriginalFilename());
        response.setDownloadUri(filePath);
        response.setSize(file.getSize());

        return new ResponseEntity<>(response, HttpStatus.OK); // Trả về phản hồi thành công
    }
	
	// Upload logo công ty
	@PostMapping("/upload-company")
    public ResponseEntity<FileUploadResponse> uploadCompanyLogo(@RequestParam("file") MultipartFile file,
                                                                @RequestParam("email") String email) throws IOException {
        String filePath = fileUploadService.saveFile(file.getOriginalFilename(), file, email);
        fileUploadService.uploadCompanyLogo(email, filePath); // Lưu và liên kết logo với công ty

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(file.getOriginalFilename());
        response.setDownloadUri(filePath);
        response.setSize(file.getSize());

        return new ResponseEntity<>(response, HttpStatus.OK); // Trả về phản hồi thành công
    }
}

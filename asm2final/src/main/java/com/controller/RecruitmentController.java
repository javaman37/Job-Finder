package com.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dto.ResultSearchDTO;
import com.entity.ApplyPost;
import com.entity.Category;
import com.entity.Company;
import com.entity.Recruitment;
import com.entity.User;
import com.request.RecruitmentRequest;
import com.service.ApplyPostService;
import com.service.CategoryService;
import com.service.RecruitmentService;
import com.service.UserService;

@Controller
public class RecruitmentController {

	private final UserService userService;

	@Autowired
	private RecruitmentService recruitmentService;

	private final CategoryService categoryService;
	
	
	private final ApplyPostService applyPostService;


	public RecruitmentController(UserService userService, RecruitmentService recruitmentService,
			CategoryService categoryService,ApplyPostService applyPostService) {
		this.userService = userService;
		this.recruitmentService = recruitmentService;
		this.categoryService = categoryService;
		this.applyPostService = applyPostService;
	}

	@GetMapping("/recruitment/detail/{id}")
	public String viewRecruitmentDetail(@PathVariable Long id, Model model, HttpSession session) {
		Recruitment recruitment = recruitmentService.getRecruitmentById(id);

		// Định dạng createdAt
		String createdAtFormatted = formatCreatedAt(recruitment.getCreatedAt());

		// Định dạng expiryDate
		String deadlineFormatted = formatDeadline(recruitment.getDeadline());

		// Thêm thông tin về user hiện tại vào model (nếu chưa có)
		User currentUser = (User) session.getAttribute("user");
		if (currentUser != null) {
			model.addAttribute("user", currentUser);
		}
		
		// Retrieve the list of users who have applied for this recruitment
	    List<ApplyPost> applyPosts = applyPostService.findByRecruitmentId(id);

		model.addAttribute("recruitment", recruitment);
		model.addAttribute("applyPosts", applyPosts);
		model.addAttribute("createdAtFormatted", createdAtFormatted);
		model.addAttribute("deadlineFormatted", deadlineFormatted);
		return "detail-post"; // Đường dẫn đến trang chi tiết bài đăng
	}

	@GetMapping("/recruitment/editpost/{id}")
	public String editRecruitment(@PathVariable Long id, Model model) {
		Recruitment recruitment = recruitmentService.getRecruitmentById(id);

		RecruitmentRequest recruitmentRequest = new RecruitmentRequest();
		recruitmentRequest.setId(recruitment.getId());
		recruitmentRequest.setTitle(recruitment.getTitle());
		recruitmentRequest.setDescription(recruitment.getDescription());
		recruitmentRequest.setExperience(recruitment.getExperience());
		recruitmentRequest.setQuantity(recruitment.getQuantity());
		recruitmentRequest.setAddress(recruitment.getAddress());
		recruitmentRequest.setDeadline(recruitment.getDeadline());
		recruitmentRequest.setSalary(recruitment.getSalary());
		recruitmentRequest.setType(recruitment.getType());
		// Kiểm tra nếu Category không phải là null
		if (recruitment.getCategory() != null) {
			recruitmentRequest.setCategoryId(recruitment.getCategory().getId());
		}

		// Lấy danh sách category
		List<Category> categories = categoryService.getAllCategories();

		// Thêm vào Model
		model.addAttribute("recruitment", recruitment);
		model.addAttribute("recruitmentRequest", recruitmentRequest);
		model.addAttribute("categories", categories);

		return "edit-job"; // Trả về tên của template

	}

	@PostMapping("/recruitment/delete")
	public String deleteRecruitment(@RequestParam Long id) {
		recruitmentService.deleteRecruitment(id);
		return "redirect:/user/list-post"; // Sau khi xóa sẽ chuyển hướng về danh sách bài đăng
	}

	@GetMapping("/post-job")
	public String showPostJobForm(Model model) {
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		return "post-job"; // Đây là tên file HTML đã tải lên
	}

	// dang tuyen
	@PostMapping("/recruitment/add")
	public String addRecruitment(@ModelAttribute Recruitment recruitment, Model model,
			@RequestParam("category_id") Long categoryId, Principal principal) {
		// Lấy thông tin email của user hiện tại
		String email = principal.getName();
		User user = userService.findUserByEmail(email);

		// Lấy thông tin công ty của user (HR)
		Company company = user.getCompany(); // Giả định rằng User có một liên kết với Company

		if (company == null) {
			// Handle case where user is not associated with any company
			model.addAttribute("error", "Bạn chưa được liên kết với công ty nào.");
			return "redirect:/error-page"; // Redirect to an error page
		}

		// Lấy thông tin danh mục công việc
		Category category = categoryService.getCategoryById(categoryId);

		if (category == null) {
			model.addAttribute("error", "Danh mục công việc không hợp lệ.");
			return "redirect:/error-page";
		}

		// Gán công ty & danh mục hiện tại làm người tạo bài tuyển dụng
		recruitment.setCompany(company);
		recruitment.setCategory(category);
		recruitmentService.saveRecruitment(recruitment);

		// Chuyển hướng về trang danh sách hoặc thông báo thành công
		model.addAttribute("success", true);
		return "redirect:/user/list-post";
	}

	
	@PostMapping("/recruitment/update")
	public String updateRecruitment(@ModelAttribute("recruitment") RecruitmentRequest recruitmentRequest, Model model) {
	    // Lấy đối tượng Recruitment từ database
	    Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentRequest.getId());
	    
	    // Cập nhật các giá trị từ RecruitmentRequest vào đối tượng Recruitment
	    recruitment.setTitle(recruitmentRequest.getTitle());
	    recruitment.setDescription(recruitmentRequest.getDescription());
	    recruitment.setExperience(recruitmentRequest.getExperience());
	    recruitment.setQuantity(recruitmentRequest.getQuantity());
	    recruitment.setAddress(recruitmentRequest.getAddress());
	    recruitment.setDeadline(recruitmentRequest.getDeadline());
	    recruitment.setSalary(recruitmentRequest.getSalary());
	    recruitment.setType(recruitmentRequest.getType());
	    
	    // Cập nhật danh mục công việc (category)
	    Category category = categoryService.getCategoryById(recruitmentRequest.getCategoryId());
	    recruitment.setCategory(category);
	    
	    // Lưu đối tượng Recruitment đã chỉnh sửa vào cơ sở dữ liệu
	    recruitmentService.saveRecruitment(recruitment);
	    model.addAttribute("recruitmentRequest", recruitmentRequest);
	    
	    // Thêm thông báo thành công vào model
	    model.addAttribute("success", "Cập nhật thành công!");
	    
	    // Trả về trang hiện tại với thông tin đã cập nhật
	    return "redirect:/user/list-post";
	}

	@GetMapping("/recruitment/search")
	public String searchRecruitment(
	        @RequestParam("keySearch") String keySearch,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,
	        Model model) {
	    Pageable pageable = PageRequest.of(page, size);
	    ResultSearchDTO resultPage = recruitmentService.searchByTitle(keySearch, pageable);

	    model.addAttribute("list", resultPage);
	    model.addAttribute("keySearch", keySearch);
	    model.addAttribute("numberPage", resultPage.getNumber());

	    return "result-search"; // Your result page
	}
	
	
	
	@GetMapping("/recruitment/searchcompany")
	public String searchRecruitmentByCompanyName(
	        @RequestParam("keySearch") String keySearch,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,
	        Model model) {
	    Pageable pageable = PageRequest.of(page, size);
	    ResultSearchDTO resultPage = recruitmentService.searchByCompanyName(keySearch, pageable);

	    model.addAttribute("list", resultPage);
	    model.addAttribute("keySearch", keySearch);
	    model.addAttribute("numberPage", resultPage.getNumber());

	    return "result-search-company"; // Your result page
	}
	
	@GetMapping("/recruitment/searchaddress")
	public String searchRecruitmentByAddress(
	        @RequestParam("keySearch") String keySearch,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,
	        Model model) {
	    Pageable pageable = PageRequest.of(page, size);
	    ResultSearchDTO resultPage = recruitmentService.searchByAddress(keySearch, pageable);

	    model.addAttribute("list", resultPage);
	    model.addAttribute("keySearch", keySearch);
	    model.addAttribute("numberPage", resultPage.getNumber());

	    return "result-search-address"; // Your result page
	}
	
	
	private String formatCreatedAt(String createdAt) {
		
		if (createdAt == null || createdAt.isEmpty()) {
	        return createdAt; // Return the original string if it's null or empty
	    }
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ban đầu của chuỗi
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy"); // Định dạng bạn muốn hiển thị

		try {
			Date date = inputFormat.parse(createdAt);
			return outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return createdAt; // Nếu lỗi, trả về chuỗi ban đầu
		}
	}

	private String formatDeadline(String deadline) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ban đầu của chuỗi
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy"); // Định dạng hiển thị

		try {
			Date date = inputFormat.parse(deadline);
			return outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return deadline; // Nếu lỗi, trả về chuỗi ban đầu
		}
	}

}

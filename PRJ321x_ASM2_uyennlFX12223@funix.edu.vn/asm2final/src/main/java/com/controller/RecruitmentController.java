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
			CategoryService categoryService, ApplyPostService applyPostService) {
		this.userService = userService;
		this.recruitmentService = recruitmentService;
		this.categoryService = categoryService;
		this.applyPostService = applyPostService;
	}

	// Hiển thị chi tiết bài đăng tuyển dụng
	@GetMapping("/recruitment/detail/{id}")
	public String viewRecruitmentDetail(@PathVariable Long id, Model model, HttpSession session) {
		Recruitment recruitment = recruitmentService.getRecruitmentById(id);

		// Định dạng ngày tạo và hạn chót
		String createdAtFormatted = formatCreatedAt(recruitment.getCreatedAt());
		String deadlineFormatted = formatDeadline(recruitment.getDeadline());

		// Thêm thông tin người dùng hiện tại vào model nếu có
		User currentUser = (User) session.getAttribute("user");
		if (currentUser != null) {
			model.addAttribute("user", currentUser);
		}
		
		// Lấy danh sách ứng viên đã ứng tuyển cho bài đăng này
	    List<ApplyPost> applyPosts = applyPostService.findByRecruitmentId(id);

		model.addAttribute("recruitment", recruitment);
		model.addAttribute("applyPosts", applyPosts);
		model.addAttribute("createdAtFormatted", createdAtFormatted);
		model.addAttribute("deadlineFormatted", deadlineFormatted);
		return "detail-post"; // Trả về trang chi tiết bài đăng
	}

	// Hiển thị trang chỉnh sửa bài đăng tuyển dụng
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
		
		// Nếu có danh mục, gán giá trị cho request
		if (recruitment.getCategory() != null) {
			recruitmentRequest.setCategoryId(recruitment.getCategory().getId());
		}

		// Lấy danh sách danh mục công việc
		List<Category> categories = categoryService.getAllCategories();

		model.addAttribute("recruitment", recruitment);
		model.addAttribute("recruitmentRequest", recruitmentRequest);
		model.addAttribute("categories", categories);

		return "edit-job"; // Trả về trang chỉnh sửa bài đăng
	}

	// Xóa bài đăng tuyển dụng
	@PostMapping("/recruitment/delete")
	public String deleteRecruitment(@RequestParam Long id) {
		recruitmentService.deleteRecruitment(id);
		return "redirect:/user/list-post"; // Chuyển về danh sách bài đăng sau khi xóa
	}

	// Hiển thị trang đăng bài tuyển dụng
	@GetMapping("/post-job")
	public String showPostJobForm(Model model) {
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		return "post-job"; // Trả về trang đăng bài tuyển dụng
	}

	// Thêm bài tuyển dụng mới
	@PostMapping("/recruitment/add")
	public String addRecruitment(@ModelAttribute Recruitment recruitment, Model model,
			@RequestParam("category_id") Long categoryId, Principal principal) {
		// Lấy email của người dùng hiện tại
		String email = principal.getName();
		User user = userService.findUserByEmail(email);

		// Lấy công ty của người dùng
		Company company = user.getCompany(); 

		if (company == null) {
			// Nếu người dùng không có công ty liên kết
			model.addAttribute("error", "Bạn chưa được liên kết với công ty nào.");
			return "redirect:/error-page"; 
		}

		// Lấy danh mục công việc
		Category category = categoryService.getCategoryById(categoryId);

		if (category == null) {
			model.addAttribute("error", "Danh mục công việc không hợp lệ.");
			return "redirect:/error-page";
		}

		// Gán công ty và danh mục cho bài đăng
		recruitment.setCompany(company);
		recruitment.setCategory(category);
		recruitmentService.saveRecruitment(recruitment);

		model.addAttribute("success", true);
		return "redirect:/user/list-post"; // Chuyển về danh sách bài đăng
	}

	// Cập nhật bài tuyển dụng
	@PostMapping("/recruitment/update")
	public String updateRecruitment(@ModelAttribute RecruitmentRequest recruitmentRequest, Model model) {
	    Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentRequest.getId());
	    
	    // Cập nhật các thông tin từ RecruitmentRequest
	    recruitment.setTitle(recruitmentRequest.getTitle());
	    recruitment.setDescription(recruitmentRequest.getDescription());
	    recruitment.setExperience(recruitmentRequest.getExperience());
	    recruitment.setQuantity(recruitmentRequest.getQuantity());
	    recruitment.setAddress(recruitmentRequest.getAddress());
	    recruitment.setDeadline(recruitmentRequest.getDeadline());
	    recruitment.setSalary(recruitmentRequest.getSalary());
	    recruitment.setType(recruitmentRequest.getType());
	    
	    // Cập nhật danh mục công việc
	    Category category = categoryService.getCategoryById(recruitmentRequest.getCategoryId());
	    recruitment.setCategory(category);
	    
	    recruitmentService.saveRecruitment(recruitment);
	    model.addAttribute("recruitmentRequest", recruitmentRequest);
	    model.addAttribute("success", "Cập nhật thành công!");
	    
	    return "redirect:/user/list-post"; // Trả về trang danh sách bài đăng đã cập nhật
	}

	// Tìm kiếm bài tuyển dụng theo tiêu đề
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

	    return "result-search"; // Trả về trang kết quả tìm kiếm
	}

	// Tìm kiếm bài tuyển dụng theo tên công ty
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

	    return "result-search-company"; // Trả về trang kết quả tìm kiếm theo công ty
	}

	// Tìm kiếm bài tuyển dụng theo địa chỉ
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

	    return "result-search-address"; // Trả về trang kết quả tìm kiếm theo địa chỉ
	}
	
	// Định dạng ngày tạo
	private String formatCreatedAt(String createdAt) {
		if (createdAt == null || createdAt.isEmpty()) {
	        return createdAt; // Trả về chuỗi ban đầu nếu null hoặc rỗng
	    }
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");

		try {
			Date date = inputFormat.parse(createdAt);
			return outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return createdAt; // Nếu lỗi, trả về chuỗi ban đầu
		}
	}

	// Định dạng hạn chót
	private String formatDeadline(String deadline) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");

		try {
			Date date = inputFormat.parse(deadline);
			return outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return deadline; // Nếu lỗi, trả về chuỗi ban đầu
		}
	}

}

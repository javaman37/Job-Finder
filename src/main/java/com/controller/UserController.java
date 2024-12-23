package com.controller;

import java.security.Principal;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.entity.ApplyPost;
import com.entity.Company;
import com.entity.Cv;
import com.entity.FollowCompany;
import com.entity.Recruitment;
import com.entity.SaveJob;
import com.entity.User;
import com.service.ApplyPostService;
import com.service.CompanyService;
import com.service.CvService;
import com.service.EmailService;
import com.service.RecruitmentService;
import com.service.SaveJobService;
import com.service.UserService;
import com.service.FollowCompanyService;

@Controller
public class UserController {
	
	@Autowired
    private ApplyPostService applyPostService;

    @Autowired
    private CvService cvService;

	private final UserService userService;

	private final CompanyService companyService;
	
	private final FollowCompanyService followCompanyService;

	@Autowired
	private RecruitmentService recruitmentService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
    private SaveJobService saveJobService;

	public UserController(UserService userService, CompanyService companyService, FollowCompanyService followCompanyService) {
		this.userService = userService;
		this.companyService = companyService;
		this.followCompanyService = followCompanyService;
	}

	// Gửi mã xác thực qua email
	@PostMapping("user/send-verification-token")
	public String sendVerificationToken(@RequestParam("email") String email) {
		User user = userService.findUserByEmail(email);

		if (user != null) {
			String token = UUID.randomUUID().toString();
			userService.createVerificationToken(user, token);

			String recipientAddress = user.getEmail();
			String subject = "Xác Thực Đăng Ký";
			String confirmationUrl = "http://localhost:8080/asm2final/auth/confirm?token=" + token;
			String message = "Nhấp vào đường link để xác thực đăng ký của bạn: " + confirmationUrl;

			emailService.sendSimpleMessage(recipientAddress, subject, message);
		}
		return "redirect:/user/profile";
	}

	// Hiển thị thông tin cá nhân của người dùng
	@GetMapping("/user/profile")
	public String userProfile(@AuthenticationPrincipal UserDetails userDetails, Model model, HttpSession session) {
		if (userDetails != null) {
			User user = userService.findUserByEmail(userDetails.getUsername());
			if (user == null) {
				throw new UsernameNotFoundException("User not found");
			}

			Company company;
			if (user.getCompany() != null) {
				company = companyService.getCompanyById(user.getCompany().getId());
			} else {
				company = new Company();
				company.setUser(user);
			}
			Cv cv = null;
	        if (user.getCv() != null) {
	            cv = cvService.findById(user.getCv().getId());
	        }
			model.addAttribute("Cv", cv);
			model.addAttribute("userInformation", user);
			model.addAttribute("companyInformation", company);
		}
		return "profile";
	}

	// Cập nhật thông tin cá nhân của người dùng
	@PostMapping("/user/update-profile")
	public String updateUserProfile(@ModelAttribute("userInformation") User user) {
		userService.updateUserProfile(user);
		return "redirect:/user/profile";
	}

	// Cập nhật thông tin công ty của người dùng
	@PostMapping("/company/update-profile")
	public String updateCompanyProfile(@ModelAttribute("companyInformation") Company company, Model model) {
		Company existingCompany = companyService.getCompanyById(company.getId());

		existingCompany.setNameCompany(company.getNameCompany());
		existingCompany.setAddress(company.getAddress());
		existingCompany.setPhoneNumber(company.getPhoneNumber());
		existingCompany.setEmail(company.getEmail());
		existingCompany.setDescription(company.getDescription());

		userService.saveCompany(existingCompany);
		model.addAttribute("companyInformation", existingCompany);
		return "redirect:/user/profile";
	}
   
	// Hiển thị danh sách bài đăng của công ty (HR)
	@GetMapping("/user/list-post")
	public String listRecruitments(Model model, @RequestParam(defaultValue = "0") int page) {
		User user = userService.getCurrentUser();
        Integer companyId = (Integer) user.getCompany().getId();

	    Page<Recruitment> recruitmentList = recruitmentService.getRecruitmentsByCompany(companyId, PageRequest.of(page, 5));

	    model.addAttribute("list", recruitmentList);
	    model.addAttribute("numberPage", page);
	    model.addAttribute("totalPages", recruitmentList.getTotalPages());
	    return "post-list";
	}

	// Hiển thị danh sách bài đăng của công ty (User)
	@GetMapping("/user/company-post/{id}")
	public String getCompanyPost(@PathVariable("id") Integer companyId, Model model, @RequestParam(defaultValue = "0") int page) {
		Company company = companyService.getCompanyById(companyId);
	    if (company == null) {
	        return "redirect:/error"; // Xử lý khi công ty không tồn tại
	    }

	    Page<Recruitment> recruitmentList = recruitmentService.getRecruitmentsByCompany(companyId, PageRequest.of(page, 5));

	    model.addAttribute("list", recruitmentList);
	    model.addAttribute("numberPage", page);
	    model.addAttribute("companyId", companyId);
	    model.addAttribute("company", company); 
	    model.addAttribute("totalPages", recruitmentList.getTotalPages());
	    return "company-post";
	}

	// Người dùng nộp đơn xin việc chưa có cv trong DB
	@PostMapping("/user/apply-job")
	@ResponseBody
	public String applyJob(@RequestParam("file") MultipartFile file, 
	                       @RequestParam("idRe") Long id,
	                       @RequestParam("text") String introduction, 
	                       HttpSession session) {
	    User user = (User) session.getAttribute("user");

	    if (user == null) {
	        return "false";  // Người dùng chưa đăng nhập
	    }

	    try {
	        boolean result = userService.applyJob(file, id, introduction, user);
	        return result ? "true" : "error";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error";
	    }
	}
	
	// Nộp đơn với CV đã có sẵn
	@PostMapping("/user/apply-job1")
	@ResponseBody
	public String applyJobWithExistingCv(@RequestParam("idRe") Long idRe,
	                                     @RequestParam("text") String introduction, 
	                                     HttpSession session) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
	        return "false";  // Người dùng chưa đăng nhập
	    }
	    Long idUser = user.getId();

	    return applyPostService.applyWithExistingCv(idUser, idRe, introduction); // Nộp CV có sẵn
	}
	

	// Xóa CV của người dùng
	@PostMapping("/user/deleteCv")
	public String deleteCv(@RequestParam("idCv") Long idCv, Model model, HttpSession session) {
	    try {
	        cvService.delete(idCv);
	    } catch (DataIntegrityViolationException e) {
	        e.printStackTrace(); // Xử lý lỗi ràng buộc
	    }
        return "redirect:/user/profile"; // Quay về trang hồ sơ sau khi xóa
    }
	
	// Lưu công việc vào danh sách yêu thích
	@PostMapping("/savejob")
    @ResponseBody
    public String saveJob(@RequestParam("idRe") Long idRe, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "false"; // Người dùng chưa đăng nhập
        }

        boolean isSaved = saveJobService.saveJob(user.getId(), idRe);

        if (isSaved) {
            return "true"; // Lưu thành công
        } else {
            return "exists"; // Đã lưu trước đó
        }
    }
	
	// Hiển thị danh sách công việc đã lưu
	@GetMapping("/save-job/get-list")
    public String getListSavedJobs(Model model, @RequestParam(defaultValue = "0") int page, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        Page<SaveJob> saveJobList = saveJobService.getSavedJobs(user, page);
        model.addAttribute("saveJobList", saveJobList);
        model.addAttribute("numberPage", page);
        return "list-save-job";
    }

	// Xóa công việc đã lưu
	@GetMapping("/save-job/delete/{id}")
    public String deleteSaveJob(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        saveJobService.deleteSaveJob(id);
        redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
        return "redirect:/save-job/get-list";
    }

	// Hiển thị danh sách công việc đã nộp
	@GetMapping("/user/get-list-apply")
	public String showApplyJob(Model model, Principal principal,
	                           @RequestParam(defaultValue = "0") int page) {
	    String email = principal.getName();
	    User user = userService.findUserByEmail(email);

	    Page<ApplyPost> appliedJobsPage = applyPostService.getAppliedJobsByUser(user.getId(), page, 5);
	    
	    model.addAttribute("appliedJobs", appliedJobsPage);
	    model.addAttribute("numberPage", page);
	    
	    return "list-apply-job"; // Trả về trang hiển thị công việc đã nộp
	}

	// Hiển thị chi tiết công ty
	@GetMapping("/user/detail-company/{id}")
    public String getCompanyDetail(@PathVariable("id") int id, Model model) {
        Company company = companyService.getCompanyById(id);
        model.addAttribute("company", company);
        return "detail-company"; // Trả về trang chi tiết công ty
    }

	// Theo dõi công ty
	@PostMapping("/user/follow-company")
    @ResponseBody
    public String followCompany(@RequestParam("idCompany") Integer idCompany, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "false"; // Người dùng chưa đăng nhập
        }

        Company company = companyService.getCompanyById(idCompany);
        if (company == null) {
            return "not_found"; // Công ty không tồn tại
        }

        boolean isFollowed = followCompanyService.followCompany(user, company);

        return isFollowed ? "true" : "exists"; // Trả về kết quả theo dõi
    }

	// Hiển thị danh sách công ty đã theo dõi
	@GetMapping("/user/get-list-company")
	public String showFollowedCompanies(Model model, Principal principal,
	                           @RequestParam(defaultValue = "0") int page) {
	    String email = principal.getName();
	    User user = userService.findUserByEmail(email);

	    Page<FollowCompany> followedCompaniesPage = followCompanyService.getFollowedCompaniesByUser(user.getId(), page, 5);

	    model.addAttribute("followedCompanies", followedCompaniesPage);
	    model.addAttribute("numberPage", page);
	    
	    return "list-follow-company"; // Trả về trang hiển thị danh sách công ty đã theo dõi
	}

	// Xóa theo dõi công ty
	@GetMapping("/user/delete-follow/{id}")
    public String deleteFollowCompany(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        followCompanyService.deleteFollowCompany(id);
        redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
        return "redirect:/user/get-list-company";
    }
}

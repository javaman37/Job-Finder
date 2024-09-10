package com.controller;

import java.security.Principal;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

	public UserController(UserService userService, CompanyService companyService,FollowCompanyService followCompanyService) {
		this.userService = userService;
		this.companyService = companyService;
		this.followCompanyService = followCompanyService;
		

	}

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

	@GetMapping("/user/profile")
	public String userProfile(@AuthenticationPrincipal UserDetails userDetails, Model model,HttpSession session) {
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

	@PostMapping("/user/update-profile")
	public String updateUserProfile(@ModelAttribute("userInformation") User user) {
		userService.updateUserProfile(user);
		return "redirect:/user/profile";
	}

	@PostMapping("/company/update-profile")
	public String updateCompanyProfile(@ModelAttribute("companyInformation") Company company, Model model) {
		// Lấy thông tin công ty hiện tại từ cơ sở dữ liệu
		Company existingCompany = companyService.getCompanyById(company.getId());

		// Cập nhật thông tin công ty hiện tại với các giá trị mới từ form
		existingCompany.setNameCompany(company.getNameCompany());
		existingCompany.setAddress(company.getAddress());
		existingCompany.setPhoneNumber(company.getPhoneNumber());
		existingCompany.setEmail(company.getEmail());
		existingCompany.setDescription(company.getDescription());

		// Lưu thông tin công ty cập nhật vào cơ sở dữ liệu

		userService.saveCompany(existingCompany);
		model.addAttribute("companyInformation", existingCompany);
		return "redirect:/user/profile";
	}
   
	
	//Hiển thị danh sách bài đăng công ty của HR
	@GetMapping("/user/list-post")
	public String listRecruitments(Model model, @RequestParam(defaultValue = "0") int page) {
	 
		 // Lấy thông tin người dùng đã đăng nhập
        User user = userService.getCurrentUser();
        Integer companyId = (Integer) user.getCompany().getId();

	    // Fetch only the recruitments for the user's company with pagination
	    Page<Recruitment> recruitmentList = recruitmentService.getRecruitmentsByCompany(companyId, PageRequest.of(page, 5));

	    model.addAttribute("list", recruitmentList);
	    model.addAttribute("numberPage", page);
	    model.addAttribute("totalPages", recruitmentList.getTotalPages());
	    return "post-list";
	}
	
	//Hiển thị danh sách bài đăng công ty của User
	@GetMapping("/user/company-post/{id}")
	public String getCompanyPost(@PathVariable("id") Integer companyId,Model model, @RequestParam(defaultValue = "0") int page) {
	   
		
		Company company = companyService.getCompanyById(companyId);
	    if (company == null) {
	        // Handle case where company doesn't exist
	        return "redirect:/error"; // or handle gracefully with a message
	    }
		

	    // Fetch only the recruitments for the user's company with pagination
	    Page<Recruitment> recruitmentList = recruitmentService.getRecruitmentsByCompany(companyId, PageRequest.of(page, 5));

	    model.addAttribute("list", recruitmentList);
	    model.addAttribute("numberPage", page);
	    model.addAttribute("companyId", companyId);
	    model.addAttribute("company", company); // Pass company object to the model
	    model.addAttribute("totalPages", recruitmentList.getTotalPages());
	    return "company-post";
	}

	
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
	
	
	//nop cv có sẵn
	@PostMapping("/user/apply-job1")
	@ResponseBody
	public String applyJobWithExistingCv(@RequestParam("idRe") Long idRe,
	                                     @RequestParam("text") String introduction, 
	                                     HttpSession session) {
	    
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
	        return "false";  // Người dùng chưa đăng nhập
	    }
	    Long idUser =user.getId();

	 // Gọi phương thức từ service để xử lý apply với CV có sẵn
	    return applyPostService.applyWithExistingCv(idUser, idRe, introduction);
	}
	
	
	
	@PostMapping("/user/deleteCv")
	public String deleteCv(@RequestParam("idCv") Long idCv, Model model, HttpSession session) {

            try {
                cvService.delete(idCv);
            } catch (DataIntegrityViolationException e) {
                // Xử lý lỗi nếu xảy ra vấn đề vi phạm ràng buộc
                e.printStackTrace();
            }
       
        return "redirect:/user/profile"; // Chuyển hướng về trang hồ sơ của người dùng
    }
	
	
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
	
	@GetMapping("/save-job/delete/{id}")
    public String deleteSaveJob(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        saveJobService.deleteSaveJob(id);
        redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
        return "redirect:/save-job/get-list";
    }
	
	
	@GetMapping("/user/get-list-apply")
	public String showApplyJob(Model model, Principal principal,
	                           @RequestParam(defaultValue = "0") int page) {
	    // Get the currently logged-in user's email
	    String email = principal.getName();
	    
	    // Fetch the user by email
	    User user = userService.findUserByEmail(email);
	    
	    // Retrieve the paginated list of jobs the user applied for
	    Page<ApplyPost> appliedJobsPage = applyPostService.getAppliedJobsByUser(user.getId(), page, 5);
	    
	    // Add the applied jobs to the model to display in the view
	    model.addAttribute("appliedJobs", appliedJobsPage);
	    model.addAttribute("numberPage", page);
	    
	    return "list-apply-job"; // Return the view to display applied jobs
	}
	
	
	@GetMapping("/user/detail-company/{id}")
    public String getCompanyDetail(@PathVariable("id") int id, Model model) {
        Company company = companyService.getCompanyById(id);
        model.addAttribute("company", company); // Pass the company object to the view
        return "detail-company"; // This will map to the detail-company.html page
    }
	
	@PostMapping("/user/follow-company")
    @ResponseBody
    public String followCompany(@RequestParam("idCompany") Integer idCompany, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "false"; // User is not logged in
        }

        Company company = companyService.getCompanyById(idCompany);
        if (company == null) {
            return "not_found"; // Company does not exist
        }

        boolean isFollowed = followCompanyService.followCompany(user, company);

        if (isFollowed) {
            return "true"; // Follow successful
        } else {
            return "exists"; // Already following
        }
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/user/get-list-company")
	public String show(Model model, Principal principal,
	                           @RequestParam(defaultValue = "0") int page) {
	    // Get the currently logged-in user's email
	    String email = principal.getName();
	    
	    // Fetch the user by email
	    User user = userService.findUserByEmail(email);
	    
	    // Retrieve the paginated list of jobs the user applied for
	    Page<FollowCompany> followedCompaniesPage = followCompanyService.getFollowedCompaniesByUser(user.getId(), page, 5);
	    
	    // Add the applied jobs to the model to display in the view
	    model.addAttribute("followedCompanies", followedCompaniesPage);
	    model.addAttribute("numberPage", page);
	    
	    return "list-follow-company"; // Return the view to display applied jobs
	}
	
	@GetMapping("/user/delete-follow/{id}")
    public String deleteFollowCompany(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        followCompanyService.deleteFollowCompany(id);
        redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
        return "redirect:/user/get-list-company";
    }


}


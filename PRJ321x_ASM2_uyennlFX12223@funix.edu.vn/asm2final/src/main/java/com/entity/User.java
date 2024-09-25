package com.entity;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "image")
    private String image;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "status")
    private int status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "cv_id")// cv_id là khóa ngoại trỏ tới bảng Cv
    private Cv cv;
    
    @OneToOne(mappedBy = "user")
    private Company company;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ApplyPost> applyPosts;
    
    
    
    public List<ApplyPost> getApplyPosts() {
		return applyPosts;
	}


	public void setApplyPosts(List<ApplyPost> applyPosts) {
		this.applyPosts = applyPosts;
	}


    public User() {}
    
    // Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Cv getCv() {
		return cv;
	}

	public void setCv(Cv cv) {
		this.cv = cv;
	}
	
	// check isEnabled()
    public boolean isEnabled() {
        // Logic để xác định nếu người dùng được kích hoạt
        return status == 1; // Ví dụ: nếu status bằng 1 thì người dùng được kích hoạt
    }

    public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
    
    
}

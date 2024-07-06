package com.entity;

import javax.persistence.*;

@Entity
@Table(name = "applypost")
public class ApplyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String createdAt;
    private String nameCv;
    private int status;
    private String text;

    @ManyToOne
    @JoinColumn(name = "recruitment_id")
    private Recruiment recruitment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    protected ApplyPost() {
		// TODO Auto-generated constructor stub
	}
    
  // Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getNameCv() {
		return nameCv;
	}

	public void setNameCv(String nameCv) {
		this.nameCv = nameCv;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Recruiment getRecruitment() {
		return recruitment;
	}

	public void setRecruitment(Recruiment recruitment) {
		this.recruitment = recruitment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    
}

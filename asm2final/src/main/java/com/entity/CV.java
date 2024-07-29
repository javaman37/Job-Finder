package com.entity;

import javax.persistence.*;

@Entity
@Table(name = "cv")
public class CV {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name="file_name")
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    protected CV() {
		// TODO Auto-generated constructor stub
	}
    
    // Getters and Setters


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    
}

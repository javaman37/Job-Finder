package com.entity;

import javax.persistence.*;

@Entity
@Table(name = "cv")
public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="file_name")
    private String fileName;

    @OneToOne(mappedBy = "cv")
    private User user;
    
    public Cv() {
		// TODO Auto-generated constructor stub
	}
    
    // Getters and Setters


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

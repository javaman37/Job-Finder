package com.entity;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;
    
    @Column(name="number_choose")
    private int numberChoose;
    
    @OneToMany(mappedBy = "category")
    private Set<Recruitment> recruitments;
    
    protected Category() {
	}
    
    // Getters and Setters

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumberChoose() {
		return numberChoose;
	}
	public void setNumberChoose(int numberChoose) {
		this.numberChoose = numberChoose;
	}

    
}

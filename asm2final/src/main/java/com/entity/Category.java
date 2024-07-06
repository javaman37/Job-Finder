package com.entity;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int numberChoose;
    
    protected Category() {
		// TODO Auto-generated constructor stub
	}
    
    // Getters and Setters

	public int getId() {
		return id;
	}
	public void setId(int id) {
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

package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="category")
public class Category implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9203002144536402784L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long categoryId;
	
	@Column(name="categoryName",length=255,nullable=false)
	private String categoryName;
	
	@Column(name="isActive",nullable=false)
	private Boolean isActive;
	
	public Category() {
		isActive = true;
	}

}

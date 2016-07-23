package com.ahancer.rr.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userId;

	@Column(name="name",length=255)
	private String name;

	@Column(name="email",length=255)
	private String email;

	@Column(name="password",length=255)
	private String password;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="profilePictureId")
	private Resource profilePicture;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="bankId")
	private Bank bank;
	
	@Column(name="bankAccount",length=255)
	private String bankAccount;

	
	@Temporal(TemporalType.TIMESTAMP)
	public Date createdAt;
	
	@Column(name="createdBy")
	public Long createdBy;
	
	@Column(name="updatedBy")
	public Long updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date deletedAt;


	public User() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

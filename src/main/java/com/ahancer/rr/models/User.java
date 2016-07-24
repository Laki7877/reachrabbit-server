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
	private Date createdAt;
	
	@Column(name="createdBy")
	private Long createdBy;
	
	@Column(name="updatedBy")
	private Long updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;


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

	public Resource getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(Resource profilePicture) {
		this.profilePicture = profilePicture;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	

}

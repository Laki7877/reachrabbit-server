package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Pattern;

import com.ahancer.rr.custom.type.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name="user")
public class User extends AbstractModel implements Serializable {
	
	private static final long serialVersionUID = -8851171178921502214L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userId;

	@Column(name="name",length=255)
	private String name;

	//@NotNull(message="error.email.require")
	//@Size(min=3,max=255,message="error.email.length")
	//@Email(message="error.email.invalid")
	@Column(name="email",length=255)
	private String email;

	//@NotNull(message="error.password.require")	
	@Column(name="password",length=255)
	private String password;
	
	@Column(name="role", length=20)
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="profilePictureId")
	private Resource profilePicture;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="bankId")
	private Bank bank;
	
	@Column(name="bankAccount",length=255)
	private String bankAccount;
	
	@Column(name="phoneNumber",length=255)
	@Pattern(regexp="[0-9]*", message="error.phone")
	private String phoneNumber;
	

	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL,mappedBy="user")
	@PrimaryKeyJoinColumn
//	@JsonManagedReference
	@JsonBackReference(value="user-brand")
	private Brand brand;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL,mappedBy="user")
	@PrimaryKeyJoinColumn
//	@JsonManagedReference
	@JsonBackReference(value="user-influencer")
	private Influencer influencer;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Influencer getInfluencer() {
		return influencer;
	}

	public void setInfluencer(Influencer influencer) {
		this.influencer = influencer;
	}	
	
}

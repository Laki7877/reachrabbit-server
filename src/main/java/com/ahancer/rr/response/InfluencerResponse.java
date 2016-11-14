package com.ahancer.rr.response;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.ahancer.rr.custom.type.Gender;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.Bank;
import com.ahancer.rr.models.Category;
import com.ahancer.rr.models.Influencer;
import com.ahancer.rr.models.InfluencerMedia;
import com.ahancer.rr.models.Resource;

public class InfluencerResponse implements Serializable {

	private static final long serialVersionUID = 3006172183699657419L;
	private Long influencerId;
	private Gender gender;
	private String web;
	private String about;
	private Date birthday;
	private Set<InfluencerMedia> influencerMedias = new HashSet<InfluencerMedia>(0);
	private Set<Category> categories = new HashSet<Category>(0);
	private Bank bank;
	private String accountNumber;
	private String accountName;
	private UserResponse user;
	private String fullname;
	private String address;
	private String idCardNumber;
	private Boolean isVerify;
	private Resource idCard;
	private Double commission;
	
	public InfluencerResponse(){
		
	}
	
	public InfluencerResponse(Influencer influencer,String roleValue){
		Role role = Role.valueOf(roleValue);
		user = new UserResponse();
		user.setName(influencer.getUser().getName());
		user.setProfilePicture(influencer.getUser().getProfilePicture());
		user.setUserId(influencer.getUser().getUserId());
		switch (role) {
			case Admin:
				user.setEmail(influencer.getUser().getEmail());
				user.setPhoneNumber(influencer.getUser().getPhoneNumber());
				this.fullname = influencer.getFullname();
				this.address = influencer.getAddress();
				this.idCardNumber = influencer.getIdCardNumber();
				this.idCard = influencer.getIdCard();
				this.commission = influencer.getCommission();
				break;
			case Brand:
				break;
			case Influencer:
				user.setEmail(influencer.getUser().getEmail());
				user.setPhoneNumber(influencer.getUser().getPhoneNumber());
				this.fullname = influencer.getFullname();
				this.address = influencer.getAddress();
				this.idCardNumber = influencer.getIdCardNumber();
				this.idCard = influencer.getIdCard();
				this.commission = influencer.getCommission();
				break;
			default:
				break;
		}
		this.influencerId = influencer.getInfluencerId();
		this.about = influencer.getAbout();
		this.web = influencer.getWeb();
		this.about = influencer.getAbout();
		this.birthday = influencer.getBirthday();
		this.influencerMedias = influencer.getInfluencerMedias();
		this.categories = influencer.getCategories();
		this.bank = influencer.getBank();
		this.accountNumber = influencer.getAccountNumber();
		this.accountName = influencer.getAccountName();
		this.gender = influencer.getGender();
		this.isVerify = influencer.getIsVerify();
	}

	public Long getInfluencerId() {
		return influencerId;
	}

	public void setInfluencerId(Long influencerId) {
		this.influencerId = influencerId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Set<InfluencerMedia> getInfluencerMedias() {
		return influencerMedias;
	}

	public void setInfluencerMedias(Set<InfluencerMedia> influencerMedias) {
		this.influencerMedias = influencerMedias;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public UserResponse getUser() {
		return user;
	}

	public void setUser(UserResponse user) {
		this.user = user;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsVerify() {
		return isVerify;
	}

	public void setIsVerify(Boolean isVerify) {
		this.isVerify = isVerify;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public Resource getIdCard() {
		return idCard;
	}

	public void setIdCard(Resource idCard) {
		this.idCard = idCard;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

}

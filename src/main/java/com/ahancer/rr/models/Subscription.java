package com.ahancer.rr.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="subscription")
public class Subscription {
	
	@Id
	@Column(name="email",length=100)
	private String email;
	
	@Column(name="name",length=255)
	private String name;
	
	public Subscription() {
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

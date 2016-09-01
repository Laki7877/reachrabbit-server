package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@JsonIdentityInfo(generator=JSOGGenerator.class)
@Entity(name="completionTime")
public class CompletionTime implements Serializable {

	private static final long serialVersionUID = -7303810285357268606L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long completionId;

	@Column(name="completionTime",length=255)
	private String completionTime;
	
	@Column(name="day")
	private Integer day;

	public CompletionTime() {

	}

	public CompletionTime(Long completionId, String completionTime) {
		super();
		this.completionId = completionId;
		this.completionTime = completionTime;
	}

	public Long getCompletionId() {
		return completionId;
	}

	public void setCompletionId(Long completionId) {
		this.completionId = completionId;
	}

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

}

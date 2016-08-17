package com.ahancer.rr.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="completionTime")
public class CompletionTime implements Serializable {

	private static final long serialVersionUID = -7303810285357268606L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long completionId;

	@Column(name="completionTime",length=255)
	private String completionTime;

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

}

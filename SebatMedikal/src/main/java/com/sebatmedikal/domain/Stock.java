package com.sebatmedikal.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name = "stock")
public class Stock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;

	@Min(0)
	@Column(nullable = false)
	private int count;

	@Column(nullable = false)
	private String createdBy;

	@Column(name = "createdDate", nullable = false, updatable = false)
	private Date createdDate;

	public Stock() {
	}

	public Stock(String createdBy) {
		this.createdBy = createdBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@PrePersist
	public void setCreatedDate() {
		createdDate = new Date();
	}

	public Date getCreatedDate() {
		return createdDate;
	}
}
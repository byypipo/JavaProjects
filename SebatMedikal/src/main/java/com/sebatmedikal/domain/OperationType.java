package com.sebatmedikal.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "operation_type")
public class OperationType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, unique = true)
	private String operationTypeName;

	@Column(nullable = false)
	private boolean sale;

	@Column
	private String note;

	@Column(nullable = false)
	private String createdBy;

	@Column(name = "createdDate", nullable = false, updatable = false)
	private Date createdDate;

	public OperationType() {
		// TODO Auto-generated constructor stub
	}

	public OperationType(String operationTypeName, String createdBy, boolean isSale) {
		this.operationTypeName = operationTypeName;
		this.sale = isSale;
		this.createdBy = createdBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOperationTypeName() {
		return operationTypeName;
	}

	public void setOperationTypeName(String operationTypeName) {
		this.operationTypeName = operationTypeName;
	}

	public boolean isSale() {
		return sale;
	}

	public void setSale(boolean sale) {
		this.sale = sale;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

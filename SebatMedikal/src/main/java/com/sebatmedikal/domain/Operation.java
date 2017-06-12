package com.sebatmedikal.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name = "operation")
public class Operation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private OperationType operationType;

	@ManyToOne(fetch = FetchType.EAGER)
	private Product product;

	@Min(0)
	@Column(nullable = false)
	private int count;

	@Column(nullable = false, updatable = false)
	private BigDecimal totalPrice;

	@Column
	private String note;

	@Column(nullable = false)
	private String createdBy;

	@Column(name = "createdDate", nullable = false, updatable = false)
	private Date createdDate;

	public Operation() {
	}

	public Operation(OperationType operationType, Product product, int count, String createdBy) {
		this.operationType = operationType;
		this.product = product;
		this.count = count;
		this.createdBy = createdBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
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

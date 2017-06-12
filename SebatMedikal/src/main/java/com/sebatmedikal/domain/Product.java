package com.sebatmedikal.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, unique = true)
	private String productName;

	@Column
	private String barcod;

	@Column
	private BigDecimal price;

	@Lob
	@Column
	private byte[] image;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "brand")
	private Brand brand;

	@Column
	private String note;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "stock")
	private Stock stock;

	@Column(nullable = false)
	private String createdBy;

	@Column(name = "createdDate", nullable = false, updatable = false)
	private Date createdDate;

	public Product() {
	}

	public Product(String productName, Brand brand, String createdBy, BigDecimal price) {
		this.productName = productName;
		this.brand = brand;
		this.price = price;
		this.createdBy = createdBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getBarcod() {
		return barcod;
	}

	public void setBarcod(String barcod) {
		this.barcod = barcod;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
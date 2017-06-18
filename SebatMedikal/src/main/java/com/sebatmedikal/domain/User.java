package com.sebatmedikal.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;

	@NotEmpty
	@Column(nullable = false, unique = true)
	private String username;

	@NotEmpty
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column
	private String email;

	@Lob
	@Column
	private byte[] image;

	@Column
	private String fcmRegistrationId;

	@ManyToOne(fetch = FetchType.EAGER)
	private Role role;

	@Column
	private String note;

	@Column
	private boolean online;

	@Column(name = "lastLoginDate")
	private Date lastLoginDate;

	@Column
	private Date readedOperationsDate;
	
	@Column
	private Date readedBrandsDate;
	
	@Column
	private Date readedProductsDate;

	@Column(nullable = false)
	private String createdBy;

	@Column(name = "createdDate", nullable = false, updatable = false)
	private Date createdDate;

	public User() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getFcmRegistrationId() {
		return fcmRegistrationId;
	}

	public void setFcmRegistrationId(String fcmRegistrationId) {
		this.fcmRegistrationId = fcmRegistrationId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getNote() {
		return note;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	
	public Date getReadedOperationsDate() {
		return readedOperationsDate;
	}

	public void setReadedOperationsDate(Date readedOperationsDate) {
		this.readedOperationsDate = readedOperationsDate;
	}

	public Date getReadedBrandsDate() {
		return readedBrandsDate;
	}

	public void setReadedBrandsDate(Date readedBrandsDate) {
		this.readedBrandsDate = readedBrandsDate;
	}

	public Date getReadedProductsDate() {
		return readedProductsDate;
	}

	public void setReadedProductsDate(Date readedProductsDate) {
		this.readedProductsDate = readedProductsDate;
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

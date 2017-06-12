package com.sebatmedikal.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "role")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;

	@NotEmpty
	@Column(nullable = false, unique = true)
	private String roleName;

	@Column
	private String note;

	@Column(nullable = false)
	private String createdBy;

	@Column(name = "createdDate", nullable = false, updatable = false)
	private Date createdDate;

	public Role() {
		// TODO Auto-generated constructor stub
	}

	public Role(String roleName, String note, String createdBy) {
		this.roleName = roleName;
		this.note = note;
		this.createdBy = createdBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

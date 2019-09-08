package com.twitter.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Table(name = "User")
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User implements Serializable {
	public User() {
	}

	private static final long serialVersionUID = -2993256938657458928L;
	@Id
	@Column
	@Email
	@NotNull
	private String email;
	@Column
	@NotNull
	@JsonIgnore
	private String password;
	@Column
	@NotNull
	@Length(min = 4, max = 50)
	private String name;
	@Column
	@NotNull
	@Length(min = 4, max = 15)
	private String gender;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "User [emailAddress=" + email + ", name=" + name + ", gender=" + gender + "]";
	}
}

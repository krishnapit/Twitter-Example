package com.twitter.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
public class Message implements Serializable {
	/**
	*
	 */
	private static final long serialVersionUID = 1469674339967449323L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "message_id")
	private Integer messageId;
	@ManyToOne
	@JoinColumn(name = "email", referencedColumnName = "email")
	private User user;
	@NotNull
	@Length(min = 1, max = 100)
	@Column(name = "message")
	private String message;

	public Message() {
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

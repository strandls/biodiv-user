package com.strandls.user.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "firebase_tokens")
public class FirebaseTokens implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "token")
	private String token;
	
	public FirebaseTokens() {
		super();
	}

	public FirebaseTokens(Long userId, String token) {
		super();
		this.userId = userId;
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "FirebaseTokens [id=" + id + ", userId=" + userId + ", token=" + token + "]";
	}

}

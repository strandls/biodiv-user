package com.strandls.user.pojo.requests;

public class UserPasswordChange {

	private Long id;
	private String oldPassword;
	private String password;
	private String confirmPassword;

	public UserPasswordChange() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserPasswordChange(Long id, String oldPassword, String password, String confirmPassword) {
		super();
		this.id = id;
		this.oldPassword = oldPassword;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}

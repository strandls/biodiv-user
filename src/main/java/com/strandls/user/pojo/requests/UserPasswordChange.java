package com.strandls.user.pojo.requests;

public class UserPasswordChange {

	private Long id;
	private String oldPassword;
	private String newPassword;
	private String confirmNewPassword;

	/**
	 * 
	 */
	public UserPasswordChange() {
		super();
	}

	/**
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * @param confirmNewPassword
	 */
	public UserPasswordChange(Long id, String oldPassword, String newPassword, String confirmNewPassword) {
		super();
		this.id = id;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.confirmNewPassword = confirmNewPassword;
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

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

}

/**
 * 
 */
package com.strandls.user.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

/**
 * @author Abhishek Rudra
 *
 */
@ApiModel
@Entity
@Table(name = "suser")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6815897629561065464L;

	private Long id;
	private Boolean accountExpired;
	private Boolean accountLocked;
	private Boolean enabled;
	private String userName;
	private String aboutMe;
	private String email;
	private Boolean hideEmial;
	private String name;
	private String profilePic;
	private String icon;

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "account_expired")
	public Boolean getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(Boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	@Column(name = "account_locked")
	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	@Column(name = "enabled")
	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "username")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "about_me")
	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "hide_email_id")
	public Boolean getHideEmial() {
		return hideEmial;
	}

	public void setHideEmial(Boolean hideEmial) {
		this.hideEmial = hideEmial;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "profile_pic")
	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	@Column(name = "icon")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}

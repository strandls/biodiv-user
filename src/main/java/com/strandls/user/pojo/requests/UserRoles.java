package com.strandls.user.pojo.requests;

import java.util.Set;

import com.strandls.user.pojo.Role;

public class UserRoles {

	private Long id;
	private Boolean enabled;
	private Boolean accountExpired;
	private Boolean accountLocked;
	private Boolean passwordExpired;
	private Set<Role> roles;

	public UserRoles() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserRoles(Long id, Boolean enabled, Boolean accountExpired, Boolean accountLocked, Boolean passwordExpired,
			Set<Role> roles) {
		super();
		this.id = id;
		this.enabled = enabled;
		this.accountExpired = accountExpired;
		this.accountLocked = accountLocked;
		this.passwordExpired = passwordExpired;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(Boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public Boolean getPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(Boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}

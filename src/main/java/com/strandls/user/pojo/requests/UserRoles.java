package com.strandls.user.pojo.requests;

import java.util.Set;

import com.strandls.user.pojo.Role;

public class UserRoles {

	private Long id;
	private Set<Role> roles;

	public UserRoles() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserRoles(Long id, Set<Role> roles) {
		super();
		this.id = id;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}

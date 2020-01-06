/**
 * 
 */
package com.strandls.user.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */
@Entity
@Table(name = "user_group_member_role")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupMemberRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5160519423395168068L;
	private Long userGroupId;
	private Long roleId;
	private Long sUserId;

	@Id
	@Column(name = "user_group_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Column(name = "role_id")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "s_user_id")
	public Long getsUserId() {
		return sUserId;
	}

	public void setsUserId(Long sUserId) {
		this.sUserId = sUserId;
	}

}

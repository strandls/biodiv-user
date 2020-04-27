/**
 * 
 */
package com.strandls.user.pojo;

import java.io.Serializable;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupMemberRoleCompositeKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1230885753143316138L;
	private Long userGroupId;
	private Long roleId;
	private Long sUserId;

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getsUserId() {
		return sUserId;
	}

	public void setsUserId(Long sUserId) {
		this.sUserId = sUserId;
	}

}

/**
 * 
 */
package com.strandls.user.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class GroupAddMember {

	private Long userGroupId;
	private Long roleId;
	private List<Long> memberList;

	/**
	 * 
	 */
	public GroupAddMember() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param roleId
	 * @param memberList
	 */
	public GroupAddMember(Long userGroupId, Long roleId, List<Long> memberList) {
		super();
		this.userGroupId = userGroupId;
		this.roleId = roleId;
		this.memberList = memberList;
	}

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

	public List<Long> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<Long> memberList) {
		this.memberList = memberList;
	}

}

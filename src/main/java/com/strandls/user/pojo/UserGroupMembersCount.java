package com.strandls.user.pojo;

public class UserGroupMembersCount {

	private Long userGroupId;
	private Long count;

	/**
	 * 
	 */
	public UserGroupMembersCount() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param count
	 */
	public UserGroupMembersCount(Long userGroupId, Long count) {
		super();
		this.userGroupId = userGroupId;
		this.count = count;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}

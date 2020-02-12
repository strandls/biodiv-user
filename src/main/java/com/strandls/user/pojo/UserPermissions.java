/**
 * 
 */
package com.strandls.user.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class UserPermissions {

	private List<SpeciesPermission> allowedTaxonList;
	private List<UserGroupMemberRole> userMemberRole;
	private List<UserGroupMemberRole> userFeatureRole;
	private Boolean following;

	/**
	 * 
	 */
	public UserPermissions() {
		super();
	}

	/**
	 * @param allowedTaxonList
	 * @param userMemberRole
	 * @param userFeatureRole
	 * @param following
	 */
	public UserPermissions(List<SpeciesPermission> allowedTaxonList, List<UserGroupMemberRole> userMemberRole,
			List<UserGroupMemberRole> userFeatureRole, Boolean following) {
		super();
		this.allowedTaxonList = allowedTaxonList;
		this.userMemberRole = userMemberRole;
		this.userFeatureRole = userFeatureRole;
		this.following = following;
	}

	public List<SpeciesPermission> getAllowedTaxonList() {
		return allowedTaxonList;
	}

	public void setAllowedTaxonList(List<SpeciesPermission> allowedTaxonList) {
		this.allowedTaxonList = allowedTaxonList;
	}

	public List<UserGroupMemberRole> getUserMemberRole() {
		return userMemberRole;
	}

	public void setUserMemberRole(List<UserGroupMemberRole> userMemberRole) {
		this.userMemberRole = userMemberRole;
	}

	public List<UserGroupMemberRole> getUserFeatureRole() {
		return userFeatureRole;
	}

	public void setUserFeatureRole(List<UserGroupMemberRole> userFeatureRole) {
		this.userFeatureRole = userFeatureRole;
	}

	public Boolean getFollowing() {
		return following;
	}

	public void setFollowing(Boolean following) {
		this.following = following;
	}

}

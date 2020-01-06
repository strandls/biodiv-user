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
	 */
	public UserPermissions(List<SpeciesPermission> allowedTaxonList, List<UserGroupMemberRole> userMemberRole,
			List<UserGroupMemberRole> userFeatureRole) {
		super();
		this.allowedTaxonList = allowedTaxonList;
		this.userMemberRole = userMemberRole;
		this.userFeatureRole = userFeatureRole;
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

}

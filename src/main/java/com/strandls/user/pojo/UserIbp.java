/**
 * 
 */
package com.strandls.user.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class UserIbp {

	private Long id;
	private String name;
	private String profilePic;
	private Boolean isAdmin;

	/**
	 * 
	 */
	public UserIbp() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param profilePic
	 * @param isAdmin
	 */
	public UserIbp(Long id, String name, String profilePic, Boolean isAdmin) {
		super();
		this.id = id;
		this.name = name;
		this.profilePic = profilePic;
		this.isAdmin = isAdmin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}

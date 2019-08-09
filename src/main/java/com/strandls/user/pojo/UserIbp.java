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

	/**
	 * @param id
	 * @param name
	 * @param profilePic
	 */
	public UserIbp(Long id, String name, String profilePic) {
		super();
		this.id = id;
		this.name = name;
		this.profilePic = profilePic;
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

}

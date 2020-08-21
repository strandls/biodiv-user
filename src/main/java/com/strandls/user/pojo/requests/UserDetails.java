package com.strandls.user.pojo.requests;

public class UserDetails {

	private Long id;
	private String userName;
	private String name;
	private String aboutMe;
	private String email;
	private String sexType;
	private Double latitude;
	private Double longitude;
	private String mobileNumber;
	private String occupation;
	private String institution;
	private String location;
	private String website;

	public UserDetails() {
		super();
	}

	public UserDetails(Long id, String userName, String name, String aboutMe, String email, String sexType,
			Double latitude, Double longitude, String mobileNumber, String occupation, String institution,
			String location, String website) {
		super();
		this.id = id;
		this.userName = userName;
		this.name = name;
		this.aboutMe = aboutMe;
		this.email = email;
		this.sexType = sexType;
		this.latitude = latitude;
		this.longitude = longitude;
		this.mobileNumber = mobileNumber;
		this.occupation = occupation;
		this.institution = institution;
		this.location = location;
		this.website = website;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSexType() {
		return sexType;
	}

	public void setSexType(String sexType) {
		this.sexType = sexType;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
}

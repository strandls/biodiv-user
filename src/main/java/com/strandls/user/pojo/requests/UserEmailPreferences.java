package com.strandls.user.pojo.requests;

public class UserEmailPreferences {

	private Long id;
	private Boolean hideEmial;
	private Boolean sendNotification;
	private Boolean identificationMail;
	private Boolean sendDigest;
	private Boolean sendPushNotification;

	public UserEmailPreferences() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserEmailPreferences(Long id, Boolean hideEmial, Boolean sendNotification, Boolean identificationMail,
			Boolean sendDigest, Boolean sendPushNotification) {
		super();
		this.id = id;
		this.hideEmial = hideEmial;
		this.sendNotification = sendNotification;
		this.identificationMail = identificationMail;
		this.sendDigest = sendDigest;
		this.sendPushNotification = sendPushNotification;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getHideEmial() {
		return hideEmial;
	}

	public void setHideEmial(Boolean hideEmial) {
		this.hideEmial = hideEmial;
	}

	public Boolean getSendNotification() {
		return sendNotification;
	}

	public void setSendNotification(Boolean sendNotification) {
		this.sendNotification = sendNotification;
	}

	public Boolean getIdentificationMail() {
		return identificationMail;
	}

	public void setIdentificationMail(Boolean identificationMail) {
		this.identificationMail = identificationMail;
	}

	public Boolean getSendDigest() {
		return sendDigest;
	}

	public void setSendDigest(Boolean sendDigest) {
		this.sendDigest = sendDigest;
	}

	public Boolean getSendPushNotification() {
		return sendPushNotification;
	}

	public void setSendPushNotification(Boolean sendPushNotification) {
		this.sendPushNotification = sendPushNotification;
	}
}

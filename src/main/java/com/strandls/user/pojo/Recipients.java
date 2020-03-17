package com.strandls.user.pojo;

public class Recipients {
	
	private Long id;
	private String name;
	private String email;
	private Boolean isSubscribed;
	private String firebaseSubscriptionKey;
	
	public Recipients() {
		super();
	}
	
	public Recipients(Long id, String name, String email, Boolean isSubscribed, String gcmKey) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.isSubscribed = isSubscribed;
		this.firebaseSubscriptionKey = gcmKey;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsSubscribed() {
		return isSubscribed;
	}

	public void setIsSubscribed(Boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}

	public String getFirebaseSubscriptionKey() {
		return firebaseSubscriptionKey;
	}

	public void setFirebaseSubscriptionKey(String gcmKey) {
		this.firebaseSubscriptionKey = gcmKey;
	}	

}

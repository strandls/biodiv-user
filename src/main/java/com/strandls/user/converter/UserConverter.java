package com.strandls.user.converter;

import java.util.ArrayList;
import java.util.List;

import com.strandls.user.dto.UserDTO;
import com.strandls.user.pojo.Recipients;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;

public class UserConverter {
	
	public static UserDTO convertToDTO(User user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setMobileNumber(user.getMobileNumber());
		return dto;
	}
	
	public static UserIbp convertToIbp(User user) {
		UserIbp ibp = new UserIbp();
		ibp.setId(user.getId());
		ibp.setName(user.getName());
		ibp.setProfilePic(user.getProfilePic());
		return ibp;
	}
	
	public static List<UserIbp> convertToIbpList(List<User> users) {
		List<UserIbp> ibpList = new ArrayList<UserIbp>();
		for (User user: users) {
			ibpList.add(convertToIbp(user));
		}
		return ibpList;
	}
	
	public static Recipients convertToRecipient(User user) {
		Recipients recipients = new Recipients();
		recipients.setId(user.getId());
		recipients.setName(user.getName());
		recipients.setEmail(user.getEmail());
		recipients.setIsSubscribed(user.getSendNotification());
		recipients.setFirebaseSubscriptionKey("");
		return recipients;
	}
	
	public static List<Recipients> convertToRecipientList(List<User> users) {
		List<Recipients> recipientList = new ArrayList<>();
		for (User user: users) {
			recipientList.add(convertToRecipient(user));
		}
		return recipientList;
	}

}

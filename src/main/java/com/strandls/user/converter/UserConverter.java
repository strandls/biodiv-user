package com.strandls.user.converter;

import com.strandls.user.dto.UserDTO;
import com.strandls.user.pojo.User;

public class UserConverter {
	
	public static UserDTO convertToDTO(User user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setMobileNumber(user.getMobileNumber());
		return dto;
	}

}

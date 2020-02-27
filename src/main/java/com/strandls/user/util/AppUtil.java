package com.strandls.user.util;

import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;

public class AppUtil {
	
	public static enum VERIFICATION_TYPE {
		EMAIL,
		MOBILE
	};
	
	public static enum VERIFICATION_ACTIONS {
		USER_REGISTRATION(0),
		FORGOT_PASSWORD(1);
		
		private int action = -1;
		
		private VERIFICATION_ACTIONS(int action) {
			this.action = action;
		}
		
		public int getAction() {
			return action;
		}
	};
	
	public static enum AUTH_MODE {
		MANUAL("manual"), 
		OAUTH_GOOGLE("oauth-google");
		
		private String action = null;
		
		private AUTH_MODE(String action) {
			this.action = action;
		}
		
		public String getAction() {
			return action;
		}
	};
	
	public static String getVerificationType(String selectedType) {
	    for (VERIFICATION_TYPE type: VERIFICATION_TYPE.values()) {
	        if (type.name().equalsIgnoreCase(selectedType)) {
	            return type.name();
	        }
	    }
	    return null;		
	}
	
	public static String getVerificationAction(int selectedAction) {
	    for (VERIFICATION_ACTIONS action: VERIFICATION_ACTIONS.values()) {
	        if (action.getAction() == selectedAction) {
	            return action.name();
	        }
	    }
	    return null;		
	}
	
	public static String encodeString(String data) {
		return new String(Base64.getEncoder().encode(data.getBytes()));
	}
	
	public static String decodeString(String data) {
		return new String(Base64.getDecoder().decode(data.getBytes()));
	}
	
	public static String generateOTP() {
		Random random = new Random();
		return String.format("%06d", random.nextInt(1000000));
	}

	public static String buildURI(HttpServletRequest request, String path, Map<String, String> params, boolean includeCtxPath) throws URISyntaxException {
		StringBuilder url = new StringBuilder();
		String scheme = request.getScheme();
		String serverName = request.getServerName();
        String contextPath = request.getContextPath();
        url.append(scheme).append("://").append(serverName);
        if (includeCtxPath == true)
            url.append(contextPath);
        url.append(path);

        URIBuilder builder = new URIBuilder(url.toString());
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                if (!key.isEmpty()) {
                    String value = entry.getValue();
                    builder.addParameter(key, value);
                }
            }
        }
        return builder.build().toString();
	}
	
	public static String capitalize(String name) {
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

}

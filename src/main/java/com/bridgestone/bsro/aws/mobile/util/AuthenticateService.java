package com.bridgestone.bsro.aws.mobile.util;

import java.io.IOException;
import java.util.Properties;

public class AuthenticateService {
	public boolean authenticateService(Properties properties, String tokenId) throws IOException {
		if(tokenId!=null && !tokenId.equals("") && properties.containsValue(tokenId)) {
			return true;
		}
		return false;
	}
}

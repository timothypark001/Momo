package com.momo.auth;

import java.util.Map;

public class OAuth2ResponseNaver implements OAuth2Response {

    private final Map<String, Object> attribute;

    public OAuth2ResponseNaver(Map<String, Object> attribute) {

        this.attribute = (Map<String, Object>) attribute.get("response");
    }
	
	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getProviderId() {
		return attribute.get("id").toString();
	}

	@Override
	public String getEmail() {
		return attribute.get("email").toString();
	}

	@Override
	public String getName() {
		return attribute.get("name").toString();
	}
	

}

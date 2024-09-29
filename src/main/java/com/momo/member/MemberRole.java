package com.momo.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum MemberRole {
	
	ADMIN("ROLE_ADMIN"),
	MEMBER("ROLE_MEMBER"),
//	OAuth2 가입 사용자
	SOCIAL("ROLE_SOCIAL");
	
	MemberRole(String value) {
		this.value = value;
	}

	private String value;
	
}

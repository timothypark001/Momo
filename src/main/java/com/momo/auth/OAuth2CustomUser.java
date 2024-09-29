package com.momo.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.momo.member.Member;

public class OAuth2CustomUser implements OAuth2User {


	private final OAuth2Response oAuth2Response;
	private final String role;
	private final Member member;
	
	public OAuth2CustomUser(OAuth2Response oAuth2Response, String role, Member member) {
		this.oAuth2Response = oAuth2Response;
		this.role = role;
		this.member = member;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return role;
			}
		});
		
		return collection;
	}

	@Override
	public String getName() {
		return this.member.getMemberid();
	}
	
	public String getMemberid() {
		return this.member.getMemberid();
	}
	
	
	public String getMembernick() {
		return this.member.getMembernick();
	}
	
	public String getUsername() {
		return this.member.getMemberid();
	}
	
}

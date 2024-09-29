package com.momo.member;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class MemberDetail extends User {
	
	private final String membernick;
	
	private final String role;
	
    public MemberDetail(String username, String password, Collection<? extends GrantedAuthority> authorities, String membernick) {
        super(username, password, authorities);
        this.membernick = membernick;
        this.role = authorities.toString();
    }

}

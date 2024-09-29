package com.momo.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberAddRequest {
	
	private String memberid;
	
	private String membernick;
	
	private String membername;
	
	private String password1;

	private String password2;

	private String email;

}

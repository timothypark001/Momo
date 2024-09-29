package com.momo.member;

import org.eclipse.angus.mail.handlers.text_html;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberCreateForm {
	
	@Size(min = 3, max = 30)
	@NotEmpty(message = "아이디는 필수 항목입니다")
	private String memberid;
	
	@Size(min = 1, max = 30)
	@NotEmpty(message = "닉네임은 필수 항목입니다")
	private String membernick;
	
	@Size(min = 1, max = 30)
	@NotEmpty(message = "이름(실명)은 필수 항목입니다")
	private String membername;
	
	@Size(min = 3)
	@NotEmpty(message = "비밀번호는 필수 항목입니다")
	private String password1;

	@Size(min = 3)
	@NotEmpty(message = "비밀번호 확인은 필수입니다" )
	private String password2;

	@Size(min = 3)
	@Email
	@NotEmpty(message = "이메일은 필수 항목입니다")
	private String email;
	
	private String gender;
	
	private String mbti;
	
	@Column(columnDefinition = "TEXT")
	private String content;


}

package com.momo.mail;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class MailContent {
	
	private String mailAddress;
	
	private String subject;
	
	private String content;
	

}

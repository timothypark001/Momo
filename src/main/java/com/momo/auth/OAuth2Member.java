package com.momo.auth;

import java.time.LocalDateTime;

import com.momo.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class OAuth2Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	private String provider;
	
	private String providerId ;
	
	private LocalDateTime connectDate;
	
	@ManyToOne
	private Member member;


}

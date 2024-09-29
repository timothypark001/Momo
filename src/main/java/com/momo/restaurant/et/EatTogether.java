package com.momo.restaurant.et;

import java.time.LocalDateTime;
import java.util.List;

import com.momo.member.Member;
import com.momo.restaurant.Restaurant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class EatTogether {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	private String ettitle;
	
	private LocalDateTime regdate;
	
	private LocalDateTime etdate;
	
	@ManyToOne
	private Member applymember;
	
	@ManyToMany
	private List<Member> prtmember;
	
	@ManyToOne
	private Restaurant rest;
	
	private String prtnumber;
	
	private String prefgender;
	
	private String prefmbti;
	
	
	
}

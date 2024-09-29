package com.momo.restaurant.review;

import java.time.LocalDateTime;
import java.util.Set;

import com.momo.member.Member;
import com.momo.restaurant.Restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String membernick;
	
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
	@ManyToMany
	private Set<Member> ddabong;
	
	@ManyToMany
	private Set<Member> nope;
	
	@ManyToOne
	private Member author;
	
	@ManyToOne
	private Restaurant rest;
	
	private Integer star;

}

package com.momo.image;

import com.momo.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	private String originalFilename;
	
	private String storeFilename;
	
/*	@OneToOne(cascade = CascadeType.REMOVE)
	private Profile profile; */
	
	@OneToOne
	private Member author;
	
	
}

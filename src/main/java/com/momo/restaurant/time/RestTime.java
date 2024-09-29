package com.momo.restaurant.time;

import com.momo.restaurant.Restaurant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RestTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	private String open;
	
	private String close;
	
	private String breakStart;
	
	private String breakFinish;
	
	private String lastOrder;
	
	private String dayoff;
	
	@ManyToOne
	private Restaurant rest;
}

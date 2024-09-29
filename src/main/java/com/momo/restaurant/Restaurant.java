package com.momo.restaurant;

import java.util.List;

import org.hibernate.annotations.DynamicInsert;

import com.momo.restaurant.et.EatTogether;
import com.momo.restaurant.jjim.Jjim;
import com.momo.restaurant.review.Review;
import com.momo.restaurant.time.RestTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@NotNull
	private String name;
	
	private String category;
	
	private String addr;
		
	private String phone;
	
	private String menu;
	
	private String img;
	
	private String map1;
	
	private String map2;
	
	private Integer progresset;
	
	@Column(name="avgStar")
	private double avgStar = 0.0;

	@OneToMany(mappedBy = "rest", cascade = CascadeType.REMOVE )
	private List<Review> reviewList;
	
	@OneToMany(mappedBy = "rest" , cascade = CascadeType.REMOVE)
	private List<EatTogether> etList;
	
	@OneToMany(mappedBy = "rest", cascade = CascadeType.REMOVE )
	private List<RestTime> timeList;
	
	@OneToMany(mappedBy = "rest", cascade = CascadeType.REMOVE)
	private List<Jjim> jjimList;
}

package com.momo.restaurant.time;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.restaurant.Restaurant;

public interface TimeRepository extends JpaRepository<RestTime, Integer>{

	List<RestTime> findByRest(Restaurant rest);
	
}

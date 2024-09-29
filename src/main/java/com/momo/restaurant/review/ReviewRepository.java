package com.momo.restaurant.review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.momo.member.Member;
import com.momo.restaurant.Restaurant;

public interface ReviewRepository extends JpaRepository<Review, Integer>{

	List<Review> findByRest(Restaurant rest);
	
	@Query("select r from Review r where r.author=:author and r.content like %:content%")
	Page<Review> findByAuthorAndContent(@Param(value="author")Member author, @Param(value="content")String content, Pageable pageable);
	
	List<Review> findByAuthor(Member author);
	
	
}

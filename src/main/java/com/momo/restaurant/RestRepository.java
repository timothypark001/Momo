package com.momo.restaurant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.momo.board.ask.posting.AskPosting;
import com.momo.board.faq.posting.FaqPosting;
import com.momo.member.Member;
import com.momo.restaurant.jjim.Jjim;

public interface RestRepository extends JpaRepository<Restaurant, Integer>{

	

	Page<Restaurant>findByCategory(String category, Pageable pageable); 
	
	List<Restaurant> findAll(Specification<Restaurant> spec);
	
	Page<Restaurant> findAll(Pageable pageable);
	Page<Restaurant> findAll(Specification<Restaurant> spec , Pageable pageable);
	
	
//	@Query("select r from Restaurant r where r.name like %:kw%")
//	Page<Restaurant>findNameByKeyword(@Param("kw") String kw, Pageable pageable);
	
//	Page<Restaurant>findCategoryByKeyword(@Param("kw") String kw, Pageable pageable);
	
//	Page<Restaurant>findAddrByKeyword(@Param("kw") String kw, Pageable pageable);
	
//	Page<Restaurant>findMenuByKeyword(@Param("kw") String kw, Pageable pageable);
	

}

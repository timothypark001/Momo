package com.momo.restaurant.jjim;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.member.Member;
import com.momo.restaurant.Restaurant;

public interface JjimRepository extends JpaRepository<Jjim, Integer>{

	Optional<Jjim>  findByMemberAndRest(Member member, Restaurant rest);
	Optional<Jjim>  findByMember(Member member);
	//Optional<Jjim>  findByRest(Restaurant rest);
	List<Jjim> findByRest(Restaurant rest);
	Page<Jjim> findByMember(Member member, Pageable pageable);
	
}

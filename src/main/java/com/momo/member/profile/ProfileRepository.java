package com.momo.member.profile;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.momo.member.Member;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
	
	Optional<Profile> findByAuthor(Member member);
	
	@Query("select p from Profile p order by p.brix desc limit 30")
	List<Profile> findOrderByBrix();
	
	@Query("select p from Profile p order by p.brix desc")
	List<Profile> findAllOrderByBrix();
	
}

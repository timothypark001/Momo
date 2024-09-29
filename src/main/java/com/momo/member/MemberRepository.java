package com.momo.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	
	Optional<Member> findBymemberid(String memberid);
	Optional<Member> findMemberByEmail(String email);

}

package com.momo.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2MemberRepository extends JpaRepository<OAuth2Member, Integer> {
	
	Optional<List<OAuth2Member>> findByMemberNo(Integer memberNo);
	Optional<OAuth2Member> findByProviderId(String providerId);
	

}

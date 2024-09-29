package com.momo.member;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {
	
	private final MemberRepository memberRepository;
	
	@Override
    public Member loadUserByUsername(String memberid) {
		
        return memberRepository.findBymemberid(memberid)
                 .orElseThrow(() -> new IllegalArgumentException((memberid)));
    }

}

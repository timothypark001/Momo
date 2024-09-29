package com.momo.auth;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.momo.member.Member;
import com.momo.member.MemberRepository;
import com.momo.member.profile.Profile;
import com.momo.member.profile.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
	
	private final MemberRepository memberRepository;
	private final OAuth2MemberRepository oAuth2MemberRepository;
	private final ProfileRepository profileRepository;
	
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    	
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        String role = "ROLE_SOCIAL";
        String membernick = null;
        
        if(registrationId.equals("google")) {
        	oAuth2Response = new OAuth2ResponseGoogle(oAuth2User.getAttributes());
        } else if(registrationId.equals("naver")) {
        	oAuth2Response = new OAuth2ResponseNaver(oAuth2User.getAttributes());
        } else {
        	return null;
        }

        Optional<Member> tempMember = memberRepository.findMemberByEmail(oAuth2Response.getEmail());
        
        if(tempMember.isPresent()) {
        	Member member = tempMember.get();
        	addSnsInfo(member, oAuth2Response, registrationId);
        } else {
        	addSnsInfo(createMember(oAuth2Response), oAuth2Response, registrationId);
        }
        
        Optional<Member> tempMember2 = memberRepository.findMemberByEmail(oAuth2Response.getEmail());
        Member finalMember = tempMember2.get();
        
        if(finalMember.getCreateDate() != null) {
        	role = "ROLE_MEMBER";
        }
        
        
        return new OAuth2CustomUser(oAuth2Response, role, finalMember);
    }
    
    
	public Member createMember(OAuth2Response oAuth2Response) {

		String memberid = null;
		
		if(oAuth2Response.getProvider().equals("naver")) {
			String _memberid = oAuth2Response.getProviderId();
			memberid = "naver" + _memberid.substring(0, 10);
		} else {
			memberid = oAuth2Response.getProvider() + oAuth2Response.getProviderId();
		}
		
    	String membernameBefore = oAuth2Response.getName();
    	String membername = membernameBefore.replaceAll(" ", "");
    	
    	Member member = new Member();
    	member.setMemberid(memberid);
    	member.setMembername(membername);
    	member.setMembernick(membername);
    	member.setEmail(oAuth2Response.getEmail());
    	memberRepository.save(member);
    	
		Profile profile = new Profile();
		
		profile.setGender(null);
		profile.setMbti("");
		profile.setContent("");
		profile.setAuthor(member);
		profileRepository.save(profile);
		
		member.setProfile(profile);
		
		return member;
		
	}
	
	private OAuth2Member createOAuth2Member(Member member, OAuth2Response oAuth2Response, String registrationId) {
		
		OAuth2Member oAuth2Member = new OAuth2Member();
		oAuth2Member.setProvider(registrationId);
		oAuth2Member.setProviderId(oAuth2Response.getProviderId());
		oAuth2Member.setConnectDate(LocalDateTime.now());
		oAuth2Member.setMember(member);
		oAuth2MemberRepository.save(oAuth2Member);
		
		return oAuth2Member;
	}
	
	
	public void addSnsInfo(Member member, OAuth2Response oAuth2Response, String registrationId) {
		
		Optional<List<OAuth2Member>> snsListTemp = oAuth2MemberRepository.findByMemberNo(member.getNo());
		List<OAuth2Member> snsList = snsListTemp.get();
		
		if(member.getOauth2MemberList() == null) {
			
			createOAuth2Member(member, oAuth2Response, registrationId);
			
		} else {
			
			List<String> providerList = new ArrayList<>();
			for(int i=0 ; i < snsList.size() ; i++) {
				providerList.add(snsList.get(i).getProvider());
			}
			
			if(providerList.contains(registrationId)) {
				
			} else {
				createOAuth2Member(member, oAuth2Response, registrationId);
				
			}
		}

		memberRepository.save(member);
	
	}

	
}

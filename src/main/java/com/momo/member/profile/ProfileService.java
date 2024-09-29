package com.momo.member.profile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.board.ask.comment.AskCommentService;
import com.momo.board.ask.posting.AskPostingService;
import com.momo.board.free.comment.FreeCommentService;
import com.momo.board.free.comment.re.FreeCommentReplyService;
import com.momo.board.free.posting.FreePostingService;
import com.momo.member.Member;
import com.momo.member.MemberRepository;
import com.momo.restaurant.et.EatTogether;
import com.momo.restaurant.et.EatTogetherRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

	

	    //Repository
		private final EatTogetherRepository etRepository;
		private final ProfileRepository profileRepository;
		private final MemberRepository memberRepository;
	
	
	public void modifyProfile(String memberid, String gender, String content, String mbti){

		Optional<Member> _member = this.memberRepository.findBymemberid(memberid);
		Member member = new Member();
		if(_member.isPresent()) {
			member = _member.get();
		} else {
			throw new DataNotFoundException("회원이 없습니다");
		}
		
		Optional<Profile> _profile = this.profileRepository.findByAuthor(member);
		
		if(_profile.isEmpty()) {
			Profile profile = new Profile();
			profile.setGender(gender);
			profile.setMbti(mbti);
			profile.setContent(content);
			profile.setAuthor(member);
			this.profileRepository.save(profile);

		} else if(_profile.isPresent()) {
			Profile profile = _profile.get();
			profile.setGender(gender);
			profile.setMbti(mbti);
			profile.setContent(content);
			this.profileRepository.save(profile);
		}
		
		 
		
	}  
	
	public Profile getProfile(Member member) {
		Optional<Profile> profile = this.profileRepository.findByAuthor(member);
		if(profile.isPresent()) {
			return profile.get();
		} else {
			return null;
		}
	}
	
	public Profile getProfile(Integer no) {
		Optional<Profile> profile = this.profileRepository.findById(no);
		if(profile.isPresent()) {
			return profile.get();
		} else {
			throw new DataNotFoundException("해당 회원이 없습니다");
		}
 	}
	
	public void plusBrix(Member member) {
		Optional<Profile> _profile = this.profileRepository.findByAuthor(member);
		Profile profile = new Profile();
		if(_profile.isPresent()) {
			profile = _profile.get();
			double brix = Math.round((profile.getBrix()+0.1)*10.0)/10.0;
			profile.setBrix(brix);
			this.profileRepository.save(profile);
		} else {
			throw new DataNotFoundException("해당 회원이 없습니다");
		}
	}
	
	public void minusBrix(Member member) {
		Optional<Profile> _profile = this.profileRepository.findByAuthor(member);
		Profile profile = new Profile();
		if(_profile.isPresent()) {
			profile = _profile.get();
			double brix = Math.round((profile.getBrix()-0.1)*10.0)/10.0;
			profile.setBrix(brix);
			this.profileRepository.save(profile);
		} else {
			throw new DataNotFoundException("해당 회원이 없습니다");
		}
	}
	

	
	@Transactional
	@Scheduled(cron = "30 59 23 * * *")
	public void upBrix() {
				
		List<EatTogether> etList = this.etRepository.findAll();
		List<EatTogether> todayEtList = new ArrayList<>(); 
		
		LocalDate now = LocalDate.now();
		for(int i=0; i<etList.size(); i++) {
			if(etList.get(i).getEtdate().toLocalDate().equals(now)) {;
               Profile profile = getProfile(etList.get(i).getApplymember());
               profile.setBrix(profile.getBrix()+0.2);
               this.profileRepository.save(profile);
			   todayEtList.add(etList.get(i));
			} else {
				continue;
			}
		}
		for(int i=0; i<todayEtList.size(); i++) {
			for(int j=0; j<todayEtList.get(i).getPrtmember().size(); j++) {
				Profile profile = getProfile(todayEtList.get(i).getPrtmember().get(j));
				profile.setBrix(profile.getBrix()+0.1);
				this.profileRepository.save(profile);
				Profile prof = getProfile(todayEtList.get(i).getPrtmember().get(j));
				double p = Math.round(prof.getBrix()*10)/10.0;
				prof.setBrix(p);
				this.profileRepository.save(prof);
			}
		}
	
	}
	
	// 당도 높은 순으로 30등까지 profile 가져오기 
	public List<Profile> getListProfile() {
		return this.profileRepository.findOrderByBrix();
	}
	
	public int getMyBrixRank(Member member) {
	   List<Profile> profileRankList = this.profileRepository.findAllOrderByBrix();
	   int myRank = 0;
       for(int i=0; i<profileRankList.size(); i++) {
    	   if(profileRankList.get(i).getAuthor().equals(member)) {
    		   myRank = i+1;
    	   }
       }
       return myRank;
 	}
}

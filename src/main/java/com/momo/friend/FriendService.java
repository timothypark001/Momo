package com.momo.friend;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.momo.member.Member;
import com.momo.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {
	
	private final MemberRepository memberRepository; 


	//친구추가
	public void createFriend(String myid, Member friendMemeber) {
		
		  Optional<Member> me = this.memberRepository.findBymemberid(myid); //내 아이디 저장
		 Member mymember = me.get(); //내 정보 가져와서 member 타입으로 객체 생성
		
		 //친구 중복제거
		 /*
		 if( mymember.getFriend().contains(friendMemeber)) {
			throw new DataNotFoundException("이미 등록된 친구입니다");
		}; */
		
		 mymember.getFriend().add(friendMemeber); //친구객체를 list 컬렉션에 저장 
		 
		 this.memberRepository.save(mymember);
		 }
	
	
	
	
	//친구 삭제
	
		public void deleteFriend(Member myid, Member friendid) {
			List<Member> list = myid.getFriend();
			if(list.contains(friendid)) { 
				list.remove(friendid);
				myid.setFriend(list);
				memberRepository.save(myid);
		}
		
		} 
		
	
}

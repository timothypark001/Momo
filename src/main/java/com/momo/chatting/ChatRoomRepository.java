package com.momo.chatting;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.member.Member;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{

	Optional<ChatRoom> findByMember1AndMember2(Member member1, Member member2);
}

package com.momo.chatting;

import java.time.LocalDateTime;

import com.momo.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no;
	
	@ManyToOne
	private Member sender; //보낸사람
	
	
	private String content; //메세지 내용
	
	
	private LocalDateTime sendtime;  //보낸 시간
	
	@ManyToOne
	private ChatRoom chatroom;
	
	
}

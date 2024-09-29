package com.momo.chatting;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.momo.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no;
	
	@ManyToOne
	private Member member1;
	
	@ManyToOne
	private Member member2;
	
	@OneToMany(mappedBy = "chatroom",cascade = CascadeType.REMOVE)
	@JsonIgnore  
	private List<Message> message;
	
	
	
}

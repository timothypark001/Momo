package com.momo.board.ask.posting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;

import com.momo.board.ask.comment.AskComment;
import com.momo.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
@Entity
@DynamicInsert
public class AskPosting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	/*
	@Column(columnDefinition = "integer default 0")
	private Integer ddabong;*/
	
	@ManyToMany
	private Set<Member> nope;
	
	private String membernick;
	
	@ManyToOne
	private Member author;
	
	@Column(columnDefinition = "integer default 0")
	private Integer cnt;
	
	@OneToMany(mappedBy = "askPosting" , cascade = CascadeType.REMOVE)
	private List<AskComment> askCommentList;
	
	@ManyToMany
	private Set<Member> ddabong;
	
	private Integer ddabongCnt;
}

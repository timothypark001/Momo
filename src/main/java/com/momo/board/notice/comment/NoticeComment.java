package com.momo.board.notice.comment;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.momo.board.notice.posting.NoticePosting;
import com.momo.member.Member;

import jakarta.persistence.Column;
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
@DynamicInsert
public class NoticeComment {  //관리자만 보이게 

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no; //댓글 고유번호
	
	@Column(columnDefinition = "TEXT")
	private String content; //내용
	
	
	private String nickname; //닉네임 
	
	
	private String memberid; //아이디
	
	
	
	private LocalDateTime createDate; //작성일
	
	
	private LocalDateTime updateDate; //수정일
	
	
	
	@ColumnDefault("0")
	private Integer cnt; //조회수
	
	@ColumnDefault("0")
	private Integer ddabong; //추천
	
	@ColumnDefault("0")
	private Integer nope; //비추천
	
	
	
	
	@ManyToOne
	private NoticePosting posting;
	
	
	@ManyToOne
	private Member author;
	
	
	
}

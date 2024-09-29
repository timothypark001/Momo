package com.momo.board.free.posting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.momo.board.free.comment.FreeComment;
import com.momo.member.Member;

import jakarta.annotation.Nullable;
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

@Entity
@Setter
@Getter
@DynamicInsert
public class FreePosting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String membernick;
	
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
//	@Column(columnDefinition = "Integer default 0")
	@ColumnDefault("0")
	private Integer cnt;
	
	@OneToMany(mappedBy = "freePosting", cascade = CascadeType.REMOVE)
	private List<FreeComment> freeCommentList;
	
	@ManyToOne
	private Member author;
	
	private Set<String> ddabong;
	
	private Set<String> nope;
	
	@ColumnDefault("0")
	private int totalComment;
	
	@ColumnDefault("0")
	private int ddabongCnt;




	
	
	
}

package com.momo.board.attend.comment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.momo.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
public class AttendComment 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer attendCommentId;
	
	@Column(columnDefinition = "TEXT")
	private String content;

	private LocalDate localDate;
	
	private LocalDateTime createDate;
	
	private String membernick;
	
	private String memberid;

	@ManyToOne
	private Member author;
	
	@ColumnDefault("0")
	private Integer todaysCommentAvilivable;
}

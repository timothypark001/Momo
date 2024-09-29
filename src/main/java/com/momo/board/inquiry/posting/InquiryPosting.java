package com.momo.board.inquiry.posting;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;

import com.momo.board.inquiry.comment.InquiryComment;
import com.momo.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
public class InquiryPosting {
	
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
    
    @OneToMany(mappedBy = "inquiryPosting", cascade = CascadeType.REMOVE)
    private List<InquiryComment> commentList;
    
    @ManyToOne
    private Member author;

}

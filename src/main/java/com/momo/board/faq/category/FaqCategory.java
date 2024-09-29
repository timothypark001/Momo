package com.momo.board.faq.category;

import java.time.LocalDateTime;
import java.util.List;

import com.momo.board.faq.posting.FaqPosting;
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
public class FaqCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	private String category;
	
	@Column(unique = true)
	private String memberid;
	
	@Column(unique = true)
	private String membernick;
	
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
	
	@OneToMany(mappedBy = "faqCategory", cascade = CascadeType.REMOVE)
	private List<FaqPosting> faqPostingList;
	
	@ManyToOne
	private Member author;
	
	
}

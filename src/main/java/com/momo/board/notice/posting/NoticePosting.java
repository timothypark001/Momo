package com.momo.board.notice.posting;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.momo.board.notice.comment.NoticeComment;
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

@Entity
@Setter
@Getter
@DynamicInsert
public class NoticePosting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no; //게시글 고유번호
	
	@Column(length = 200)
	private String subject;  //제목
	
	@Column(columnDefinition = "TEXT")
	private String content; //내용
	
	

	
	
	private LocalDateTime createDate; //작성일
	
	
	private LocalDateTime updateDate; //수정일
	
	
	@Column(columnDefinition = "integer default 0", nullable = false)
	private Integer cnt;  //조회수 (관리자만 보이게)
	

	
	//comment 와 관계맺기
	@OneToMany(mappedBy = "posting",cascade = CascadeType.REMOVE)
	private List<NoticeComment> CommentList; 
	
	
	@ManyToOne
	private Member author;

	
}

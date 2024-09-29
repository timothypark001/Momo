package com.momo.board.notice.comment;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.momo.board.notice.posting.NoticePosting;
import com.momo.member.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeCommentService {  //repository 와 연결

	
	private final NoticeCommentRepository noticeCommentRepository;
	
	//답글등록 - 쿼리문
	public void create(NoticePosting noticePosting,String content,Member author) {
		NoticeComment comment = new NoticeComment();
		comment.setContent(content);
		comment.setCreateDate(LocalDateTime.now());
		comment.setPosting(noticePosting);
		comment.setAuthor(author); //작성자
		this.noticeCommentRepository.save(comment); //쿼리문 저장
		
		
		
	}
	

	
	
	
}

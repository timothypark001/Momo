package com.momo.board.inquiry.comment;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.board.inquiry.posting.InquiryPosting;
import com.momo.member.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryCommentService {

	private final InquiryCommentRepository inquiryCommentRepository;
	
	
	public InquiryComment getComment(Integer no) {
		Optional<InquiryComment> comment = this.inquiryCommentRepository.findById(no);
		if(comment.isPresent()) {
			return comment.get();
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
		
	}
	
	public void create(InquiryPosting inquiryPosting, String content, String membernick , Member member) {
		InquiryComment comment = new InquiryComment();
		comment.setContent(content);
		comment.setCreateDate(LocalDateTime.now());
		comment.setInquiryPosting(inquiryPosting);
		comment.setMembernick(membernick);
		comment.setAuthor(member);
		this.inquiryCommentRepository.save(comment);
	}
	
	public void delete(Integer no) {
		this.inquiryCommentRepository.deleteById(no);
	}
	
	public void update(Integer no, String content) {
		Optional<InquiryComment> com = this.inquiryCommentRepository.findById(no);
		if(com.isPresent()) {
			InquiryComment comment = com.get();
			comment.setContent(content);
			comment.setUpdateDate(LocalDateTime.now());
			this.inquiryCommentRepository.save(comment);
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
	}
}

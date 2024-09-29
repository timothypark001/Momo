package com.momo.board.ask.comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.board.ask.posting.AskPosting;
import com.momo.member.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AskCommentService {

	private final AskCommentRepository askCommentRepository;
	
	//답변작성 서비스구문(앵커 기능 추가를 위해 AskComment 타입으로 전환)
	public AskComment create(AskPosting askPosting , String content , Member author) {
		AskComment ac = new AskComment();
		ac.setContent(content);
		ac.setCreateDate(LocalDateTime.now());
		ac.setAskPosting(askPosting);
		ac.setAuthor(author);
		ac.setMembernick(author.getMembernick());
		this.askCommentRepository.save(ac);
		
		return ac;
	}
	
	//답변수정 서비스
	public void update(AskComment askComment , String content) {
		askComment.setContent(content);
		askComment.setUpdateDate(LocalDateTime.now());
		this.askCommentRepository.save(askComment);
	}
	
	//답변삭제 서비스
	public void delete(AskComment askComment) {
		this.askCommentRepository.delete(askComment);
	}
	
	//답변조회
	public AskComment getAskComment(Integer no) {
		Optional<AskComment> askComment = this.askCommentRepository.findById(no);
		if(askComment.isPresent()) {
			
			return askComment.get();
		}else {
			throw new DataNotFoundException("No Data");
		}
	}
	
	//마이페이지 내 답변조희 + 검색 + 페이징
		public Page<AskComment> getMyAskComment(Member member, String content, int page) { 
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("createDate"));
			Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
			return this.askCommentRepository.findByAuthorAndContent(member, content, pageable);
		}
	
	//답변 페이지화 서비스구문
	public Page<AskComment> askCommentPage(AskPosting askPosting , int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		
		Pageable pageable = PageRequest.of(page, 5);
		return askCommentRepository.findAllByAskPosting(askPosting, pageable);
	}
	
	//답변추천 서비스구문
	public void voteDdabong(AskComment askComment , Member momoMember) {
		askComment.getDdabong().add(momoMember);
		this.askCommentRepository.save(askComment);
	}
	
	//답변비추천 서비스구문
	public void voteNope(AskComment askComment , Member momoMember) {
		askComment.getNope().add(momoMember);
		this.askCommentRepository.save(askComment);
	}
	
    // 나의 추천수 총합 구하는 서비스구문
	public int getMyDdabong(Member momoMember) {
		List<AskComment> myAC = this.askCommentRepository.findByAuthor(momoMember);
		int totalDdabong = 0;
		for(int i=0; i<myAC.size(); i++) {
			totalDdabong += myAC.get(i).getDdabong().size();
		}
		return totalDdabong;
	}
	
	// 나의 비추천수 총합 구하는 서비스구문
	public int getMyNope(Member momoMember) {
		List<AskComment> myAC = this.askCommentRepository.findByAuthor(momoMember);
		int totalNope = 0;
		for(int i=0; i<myAC.size(); i++) {
			totalNope += myAC.get(i).getNope().size();
		}
		return totalNope;
	}
}

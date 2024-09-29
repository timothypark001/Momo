package com.momo.board.ask.posting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.board.ask.comment.AskComment;
import com.momo.member.Member;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AskPostingService {
	private final AskPostingRepository askPostingRepository;
	//질문글 목록 페이지형식 서비스구문
	public Page<AskPosting> getList(int page , String order , String kw){
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		//게시물 정렬 조건(최신순 , 조회순 , 추천순)
		if(order.equals("createDate")) {
			sorts.add(Sort.Order.desc("createDate"));
		}else if(order.equals("cnt")) {
			sorts.add(Sort.Order.desc("cnt"));
		}else if(order.equals("ddabong")) {
			sorts.add(Sort.Order.desc("ddabongCnt"));
		}
		Pageable pageable = PageRequest.of(page ,  10 , Sort.by(sorts));
		Specification<AskPosting> spec = search(kw);
		return this.askPostingRepository.findAll(spec , pageable);
	}
	
	//나의 질문과답변 글 목록 페이지형식+검색 서비스구문
		public Page<AskPosting> getMyList(Member author, String subject, int page) {
			List<Sort.Order> sorts = new ArrayList<Sort.Order>();
			sorts.add(Sort.Order.desc("createDate"));
			Pageable pageable = PageRequest.of(page ,  5 , Sort.by(sorts));
			return this.askPostingRepository.findByAuthorAndSubject(author, subject, pageable);
		}
		
	//질문글 조회 서비스구문
	public AskPosting getAskPosting(Integer no) {
		Optional<AskPosting> askPosting = this.askPostingRepository.findById(no);
		if(askPosting.isPresent()) {
			AskPosting ap = askPosting.get();
			ap.setCnt(ap.getCnt() + 1);
			return this.askPostingRepository.save(ap);
		}else {
			throw new DataNotFoundException("No Data");
		}
	}
	
	//질문글 작성 서비스구문
	public void create(String subject , String content , Member author) {
		AskPosting ap = new AskPosting();
		ap.setSubject(subject);
		ap.setContent(content);
		ap.setAuthor(author);
		ap.setMembernick(author.getMembernick());
		ap.setCreateDate(LocalDateTime.now());
		this.askPostingRepository.save(ap);
	}
	
	//질문글 수정 서비스구문
	public void update(AskPosting askPosting , String subject , String content) {
		askPosting.setSubject(subject);
		askPosting.setContent(content);
		askPosting.setUpdateDate(LocalDateTime.now());
		this.askPostingRepository.save(askPosting);
	}
	
	//질문글 삭제 서비스구문
	public void delete(AskPosting askPosting) {
		this.askPostingRepository.delete(askPosting);
	}
	
	//질문글 추천 서비스구문
	public void voteDdabong(AskPosting askPosting , Member momoMember) {
		askPosting.getDdabong().add(momoMember);
		//게시물 추천수 갱신구문
		askPosting.setDdabongCnt(askPosting.getDdabong().size());
		this.askPostingRepository.save(askPosting);
	}
	
	//질문글 비추천 서비스구문
	public void voteNope(AskPosting askPosting , Member momoMember) {
		askPosting.getNope().add(momoMember);
		this.askPostingRepository.save(askPosting);
	}
	
	//검색키워드 서비스구문
	private Specification<AskPosting> search(String kw){
		return new Specification<>(){
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<AskPosting> ap , CriteriaQuery<?> query
					, CriteriaBuilder cb) {
				query.distinct(true);
				Join<AskPosting , Member> m1 = ap.join("author" , JoinType.LEFT);
				Join<AskPosting , AskComment> ac = ap.join("askCommentList" , JoinType.LEFT);
				Join<AskComment , Member> m2 = ap.join("author" , JoinType.LEFT);
				
				return cb.or(cb.like(ap.get("subject"), "%" + kw + "%"),
							cb.like(ap.get("content"), "%" + kw + "%"),
							cb.like(m1.get("membernick"), "%" + kw + "%"),
							cb.like(ac.get("content"), "%" + kw + "%"),
							cb.like(m2.get("membernick"), "%" + kw + "%"));
			}
		};
	}
	
	// 나의 추천수 총합 구하는 서비스구문
	public int getMyDdabong(Member momoMember) {
		List<AskPosting> myAP = this.askPostingRepository.findByAuthor(momoMember);
		int totalDdabong = 0;
		for(int i=0; i<myAP.size(); i++) {
			totalDdabong += myAP.get(i).getDdabong().size();
		}
		return totalDdabong;
	}
	
	// 나의 비추천수 총합 구하는 서비스구문
	public int getMyNope(Member momoMember) {
		List<AskPosting> myAP = this.askPostingRepository.findByAuthor(momoMember);
		int totalNope = 0;
		for(int i=0; i<myAP.size(); i++) {
			totalNope += myAP.get(i).getNope().size();
		}
		return totalNope;
	}
}

package com.momo.board.inquiry.posting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.board.inquiry.comment.InquiryComment;
import com.momo.member.Member;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryPostingService {

	private final InquiryPostingRepository inquiryPostingRepository;
	
	public Page<InquiryPosting> getMyList(Member member, String subject, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc(("createDate")));
		Pageable pageable = PageRequest.of(page,5, Sort.by(sorts));
		return this.inquiryPostingRepository.findByAuthorAndSubject(member, subject, pageable);
	} 
	
	public Page<InquiryPosting> getList(int page, String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<InquiryPosting> spec = search(kw);
		return this.inquiryPostingRepository.findAll(spec, pageable);
	}
	
	public Page<InquiryPosting> getNoCommenList(String subject, int page) {
		List<InquiryPosting> postingList = this.inquiryPostingRepository.findAllOrderByDesc(subject);
		List<InquiryPosting> noCommentList = new ArrayList<>();
		for(int i=0; i<postingList.size(); i++) {
			if(postingList.get(i).getCommentList().isEmpty()) {
				noCommentList.add(postingList.get(i));
			}
		}
		PageRequest pageRequest = PageRequest.of(page, 10);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), noCommentList.size());
		Page<InquiryPosting> noCommentPage = new PageImpl<>(noCommentList.subList(start, end), pageRequest, noCommentList.size());
	    return noCommentPage;
	}
	
	public InquiryPosting getInquiryPosting(Integer no) {
		Optional<InquiryPosting> posting = this.inquiryPostingRepository.findById(no);
		if(posting.isPresent()) {
		return posting.get();
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
	}
	
	public void createPosting(String subject, String content, String membernick,Member member) {
		InquiryPosting inquiryPosting = new InquiryPosting();
		inquiryPosting.setSubject(subject);
		inquiryPosting.setContent(content);
		inquiryPosting.setCreateDate(LocalDateTime.now());
		inquiryPosting.setMembernick(membernick);
		inquiryPosting.setAuthor(member);
		this.inquiryPostingRepository.save(inquiryPosting);
	}
	
	public void updatePosting(String subject, String content, Integer no) {
		Optional<InquiryPosting> posting = this.inquiryPostingRepository.findById(no);
		if(posting.isPresent()) {
	    InquiryPosting post = posting.get();
	    post.setSubject(subject);
	    post.setContent(content);
	    post.setMembernick(post.getMembernick());
	    post.setUpdateDate(LocalDateTime.now());
	    this.inquiryPostingRepository.save(post);
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
		
	}
	
	public void deletePosting(Integer no) {
		this.inquiryPostingRepository.deleteById(no);
	}
	
	private Specification<InquiryPosting> search(String kw){
		return new Specification<>(){
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<InquiryPosting> ip , CriteriaQuery<?> query
					, CriteriaBuilder cb) {
				query.distinct(true);
				
				return cb.or(cb.like(ip.get("subject"), "%" + kw + "%"));
			}
		};
	}
	

}

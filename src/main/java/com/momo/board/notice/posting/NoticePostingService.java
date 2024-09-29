package com.momo.board.notice.posting;

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
public class NoticePostingService {

	private final NoticePostingRepository noticePostingRepository;
	
	//공지사항 글 목록 - 쿼리문
	
	public Page<NoticePosting> getList(int page, String kw){
		
		//작성시간에 따른 정렬
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));   //정렬대상
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<NoticePosting> spec = search(kw); //검색 객체
		
		return this.noticePostingRepository.findAll(spec,pageable);
	}
	
	
	//공지사항 상세보기
	public NoticePosting getNoticePosting(Integer no) {
	
		Optional<NoticePosting> post = this.noticePostingRepository.findById(no);
		
		if(post.isPresent()) {
			NoticePosting noticePosting = post.get();
			noticePosting.setCnt(noticePosting.getCnt() +1); //조회수
			this.noticePostingRepository.save(noticePosting);
			return noticePosting;
		}else {
			throw new DataNotFoundException("데이터를 찾을 수 없습니다");
		}
	}
	
	
	//공지사항 글 등록
	public void create(String subject, String content,Member author) {
		NoticePosting noticePosting = new NoticePosting();
		noticePosting.setSubject(subject);
		noticePosting.setContent(content);
		noticePosting.setCreateDate(LocalDateTime.now());
		noticePosting.setAuthor(author);  
		this.noticePostingRepository.save(noticePosting);
	}
	
	
 
	//공지 수정
	public void modify(NoticePosting noticePosting,String subject, String content ) {
		
		noticePosting.setSubject(subject);
		noticePosting.setContent(content);
		noticePosting.setUpdateDate(LocalDateTime.now());
		this.noticePostingRepository.save(noticePosting);
		
		
	}
	
	
	//공지 삭제
	public void delete(NoticePosting noticePosting) {
		this.noticePostingRepository.delete(noticePosting);
	}
	
	

	//공지 검색
	private Specification<NoticePosting> search(String kw){
		return new Specification <>() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<NoticePosting> noti, CriteriaQuery<?> query, CriteriaBuilder cb) {
			
				query.distinct(true); //중복 제거
				Join<NoticePosting, Member> u1 = noti.join("author",JoinType.LEFT); //질문작성자 검색을 위해 필요
			
				return cb.or(cb.like(noti.get("subject"),"%"+kw+"%"),  //제목
					cb.like(noti.get("content"),"%"+kw+"%"),    //내용
					cb.like(u1.get("membernick"), "%"+kw+"%"));     //작성자
			}
		};
	}
}

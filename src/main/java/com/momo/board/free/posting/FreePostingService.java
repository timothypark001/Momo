package com.momo.board.free.posting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.board.free.comment.FreeComment;
import com.momo.board.free.comment.FreeCommentRepository;
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
public class FreePostingService {

	private final FreePostingRepository freePostingRepository;
	private final FreeCommentRepository freeCommentRepository;
	
	
	public void create(String subject, Member member, String membernick, String content) {
		FreePosting freePosting = new FreePosting();
		Set<String> ddabong = new HashSet<>();
		Set<String> nope = new HashSet<>();
		
		freePosting.setSubject(subject);
		freePosting.setContent(content);
		freePosting.setMembernick(membernick);
		freePosting.setCreateDate(LocalDateTime.now());
		freePosting.setAuthor(member);
		freePosting.setDdabong(ddabong);
		freePosting.setNope(nope);
		freePostingRepository.save(freePosting);
		
	}
	
	public void update(FreePosting freePosting, String subject, String content) {
		freePosting.setSubject(subject);
		freePosting.setContent(content);
		freePosting.setUpdateDate(LocalDateTime.now());
		freePostingRepository.save(freePosting);
	}
	
	public Page<FreePosting> getList(int page) {
		List<Sort.Order> sort = new ArrayList<>();
		sort.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
		return freePostingRepository.findAll(pageable);
	}
	
	public List<FreePosting> getList() {
		List<FreePosting> freePostingList = new ArrayList<>();
		freePostingList = freePostingRepository.findAll();
		return freePostingList;
	}
	
	//마이페이지 나의 자유게시판 글 목록 페이지형식 + 검색 기능
    public Page<FreePosting> getMyList(Member member, String subject, int page) {	
		List<Sort.Order> sort = new ArrayList<>();
		sort.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 5, Sort.by(sort));
		return freePostingRepository.findByAuthorAndSubject(member, subject, pageable);
    }
    
    //자유게시판용 글 목록 페이지형식 + 검색 기능
    public Page<FreePosting> getList(int page , String order , String kw) {	
    	System.out.println("===검색 Service 메소드 진입===");
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
		Specification<FreePosting> spec = search(kw);
		return this.freePostingRepository.findAll(spec, pageable);
    }
	
	//검색 키워드
    @SuppressWarnings("unused")
	private Specification<FreePosting> search(String kw){
    	System.out.println("===키워드 Service 메소드 진입===");
    	
		return new Specification<>(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<FreePosting> fp , CriteriaQuery<?> query
					, CriteriaBuilder cb) {
				query.distinct(true);
				Join<FreePosting , Member> m1 = fp.join("author" , JoinType.LEFT);
				Join<FreePosting , FreeComment> fc = fp.join("freeCommentList" , JoinType.LEFT);
				Join<FreeComment , Member> m2 = fp.join("author" , JoinType.LEFT);
				
				return cb.or(cb.like(fp.get("subject"), "%" + kw + "%"),
							cb.like(fp.get("content"), "%" + kw + "%"),
							cb.like(m1.get("membernick"), "%" + kw + "%"),
							cb.like(fc.get("content"), "%" + kw + "%"),
							cb.like(m2.get("membernick"), "%" + kw + "%"));
			}
		};
	}
    
	public FreePosting getPosting(Integer no) {
		Optional<FreePosting> freePosting = freePostingRepository.findById(no);
		
		if(freePosting.isPresent()) {
			FreePosting freePostingNew = new FreePosting();
			freePostingNew = freePosting.get();
			Integer cnt = freePostingNew.getCnt();
			cnt += 1;
			freePostingNew.setCnt(cnt);
			freePostingRepository.save(freePostingNew);
			
			return freePostingNew;
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
	}
	
    public void ddabong(FreePosting freePosting, String memberid) {
    	
    	if(freePosting.getDdabong().contains(memberid)) {
    		freePosting.getDdabong().remove(memberid);
    		freePosting.setDdabongCnt(freePosting.getDdabongCnt()-1);
    	} else {
    		freePosting.getDdabong().add(memberid);
    		freePosting.setDdabongCnt(freePosting.getDdabongCnt()+1);
    	}
    	
    	freePostingRepository.save(freePosting);
    }
    
    public void nope(FreePosting freePosting, String memberid) {
    	
    	if(freePosting.getNope().contains(memberid)) {
    		freePosting.getNope().remove(memberid);
    	} else {
    		freePosting.getNope().add(memberid);
    	}
    	
    	freePostingRepository.save(freePosting);
    }

    
    // 나의 추천수 총합 구하는 서비스구문
 	public int getMyDdabong(Member momoMember) {
 		List<FreePosting> myFP = this.freePostingRepository.findByAuthor(momoMember);
 		int totalDdabong = 0;
 		for(int i=0; i<myFP.size(); i++) {
 			totalDdabong += myFP.get(i).getDdabong().size();
 		}
 		return totalDdabong;
 	}
 	
 	// 나의 비추천수 총합 구하는 서비스구문
 	public int getMyNope(Member momoMember) {
 		List<FreePosting> myFP = this.freePostingRepository.findByAuthor(momoMember);
 		int totalNope = 0;
 		for(int i=0; i<myFP.size(); i++) {
 			totalNope += myFP.get(i).getNope().size();
 		}
 		return totalNope;
 	}

    
    /*
	public FreePosting updateDdabong(Integer no) {
		Optional<FreePosting> freePosting = freePostingRepository.findById(no);
		
		if(freePosting.isPresent()) {
			FreePosting freePostingNew = new FreePosting();
			freePostingNew = freePosting.get();
			Integer ddabong = freePostingNew.getDdabong();
			ddabong += 1;
			freePostingNew.setDdabong(ddabong);
			freePostingRepository.save(freePostingNew);
			
			return freePostingNew;
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
	}
	
	public FreePosting updateNope(Integer no) {
		Optional<FreePosting> freePosting = freePostingRepository.findById(no);
		
		if(freePosting.isPresent()) {
			FreePosting freePostingNew = new FreePosting();
			freePostingNew = freePosting.get();
			Integer nope = freePostingNew.getNope();
			nope += 1;
			freePostingNew.setNope(nope);
			freePostingRepository.save(freePostingNew);
			
			return freePostingNew;
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
	}
	*/
    
	public void delete(Integer no) {
		freePostingRepository.deleteById(no);
	}
	
	// 찜 테스트를 위한 임시 메소드
	public void update(FreePosting freePosting) {
		freePostingRepository.save(freePosting);
	}
}

package com.momo.board.free.comment;

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
import org.springframework.stereotype.Service;

import com.momo.board.free.posting.FreePosting;
import com.momo.board.free.posting.FreePostingRepository;
import com.momo.member.Member;
import com.momo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreeCommentService {
	
	private final FreeCommentRepository freeCommentRepository;
	private final FreePostingRepository freePostingRepository;
	private final MemberService memberService;
	
	public List<FreeComment> getList(Integer pno) {
		
		List<FreeComment> freeCommentList = new ArrayList<>();
		freeCommentList = freeCommentRepository.findAll();
		return freeCommentList;
	}
	
	public FreeComment getComment(Integer cno) {
		Optional<FreeComment> temp = freeCommentRepository.findById(cno);
		FreeComment freeComment = temp.get();
		return freeComment;
	}
	
	//마이페이지 내 답변조회 + 검색 + 페이징
		public Page<FreeComment> getMyFreeComment(Member member, String content, int page) { 
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("createDate"));
			Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
			return this.freeCommentRepository.findByAuthorAndContent(member, content, pageable);
		}
	
	public void create(Integer pno, Member member, String content) {
		FreePosting freePosting = new FreePosting();
		Optional<FreePosting> findPosting = freePostingRepository.findById(pno);
		Set<String> ddabong = new HashSet<>();
		Set<String> nope = new HashSet<>();
		
		if(findPosting.isPresent()) {
			freePosting = findPosting.get();
			
			FreeComment freeComment = new FreeComment();
			freeComment.setFreePosting(freePosting);
			freeComment.setAuthor(member);
			freeComment.setContent(content);
			freeComment.setMembernick(member.getMembernick());
			freeComment.setCreateDate(LocalDateTime.now());
			freeComment.setDdabong(ddabong);
			freeComment.setNope(nope);
			
			freeCommentRepository.save(freeComment);
			
			freePosting.setTotalComment(freePosting.getTotalComment()+1);
			freePostingRepository.save(freePosting);
		}
		
	}
	
	public void delete(Integer pno, Integer cno) {
		Optional<FreePosting> _free = freePostingRepository.findById(pno);
		FreePosting freePosting = new FreePosting();

		freeCommentRepository.deleteById(cno);
		
		if(_free.isPresent()) {
			freePosting = _free.get();
			freePosting.setTotalComment(freePosting.getTotalComment()-1);
			freePostingRepository.save(freePosting);
		}
		
	}
	
	public void update(FreeComment freeComment, String content) {
		freeComment.setContent(content);
		freeComment.setUpdateDate(LocalDateTime.now());
		freeCommentRepository.save(freeComment);
	}
	
    public void ddabong(FreeComment freeComment, String memberid) {
    	
  		if(freeComment.getDdabong().contains(memberid)) {
    			freeComment.getDdabong().remove(memberid);
		} else {
			freeComment.getDdabong().add(memberid);
		}

    	freeCommentRepository.save(freeComment);
    }
    
    public void nope(FreeComment freeComment, String memberid) {
    	
		if(freeComment.getNope().contains(memberid)) {
			freeComment.getNope().remove(memberid);
		} else {
			freeComment.getNope().add(memberid);
		}
		
    	freeCommentRepository.save(freeComment);
    }

    // 나의 추천수 총합 구하는 서비스구문
 	public int getMyDdabong(Member momoMember) {
 		List<FreeComment> myFC = this.freeCommentRepository.findByAuthor(momoMember);
 		int totalDdabong = 0;
 		for(int i=0; i<myFC.size(); i++) {
 			totalDdabong += myFC.get(i).getDdabong().size();
 		}
 		return totalDdabong;
 	}
 	
 	// 나의 비추천수 총합 구하는 서비스구문
 	public int getMyNope(Member momoMember) {
 		List<FreeComment> myFC = this.freeCommentRepository.findByAuthor(momoMember);
 		int totalNope = 0;
 		for(int i=0; i<myFC.size(); i++) {
 			totalNope += myFC.get(i).getNope().size();
 		}
 		return totalNope;
 	}
    
}

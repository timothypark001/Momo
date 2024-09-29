package com.momo.board.free.comment.re;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.momo.board.free.comment.FreeComment;
import com.momo.board.free.comment.FreeCommentRepository;
import com.momo.board.free.posting.FreePosting;
import com.momo.board.free.posting.FreePostingRepository;
import com.momo.member.Member;
import com.momo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreeCommentReplyService {

	private final FreePostingRepository freePostingRepository;
	private final FreeCommentReplyRepository freeCommentReplyRepository;
	private final FreeCommentRepository freeCommentRepository;
	private final MemberService memberService;
	
	public FreeCommentReply getCommentReply(Integer no) {
		Optional<FreeCommentReply> temp = freeCommentReplyRepository.findById(no);
		FreeCommentReply freeCommentReply = temp.get();
		return freeCommentReply;
	}
	
	public void create(Integer pno, Integer cno, Member member, String content) {
		FreeComment freeComment = freeCommentRepository.findById(cno).get();
		Set<String> ddabong = new HashSet<>();
		Set<String> nope = new HashSet<>();
		
		FreeCommentReply freeCommentReply = new FreeCommentReply();
		freeCommentReply.setContent(content);
		freeCommentReply.setAuthor(member);
		freeCommentReply.setMembernick(member.getMembernick());
		freeCommentReply.setCreateDate(LocalDateTime.now());
		freeCommentReply.setFreeComment(freeComment);
		freeCommentReply.setDdabong(ddabong);
		freeCommentReply.setNope(nope);
		
		freeCommentReplyRepository.save(freeCommentReply);
		
		Optional<FreePosting> _free = freePostingRepository.findById(pno);
		FreePosting freePosting = new FreePosting();
		
		if(_free.isPresent()) {
			freePosting = _free.get();
			freePosting.setTotalComment(freePosting.getTotalComment()+1);
			freePostingRepository.save(freePosting);
		}
	}
	
	public void delete(Integer pno, Integer cno) {
		
		freeCommentReplyRepository.deleteById(cno);
		
		Optional<FreePosting> _free = freePostingRepository.findById(pno);
		FreePosting freePosting = new FreePosting();
		
		if(_free.isPresent()) {
			freePosting = _free.get();
			freePosting.setTotalComment(freePosting.getTotalComment()-1);
			freePostingRepository.save(freePosting);
		}
		
	}
	
	public void update(FreeCommentReply freeCommentReply, String content) {
		freeCommentReply.setContent(content);
		freeCommentReply.setUpdateDate(LocalDateTime.now());
		
		freeCommentReplyRepository.save(freeCommentReply);
	}
	
    public void ddabong(FreeCommentReply freeCommentReply, String memberid) {
    	
		if(freeCommentReply.getDdabong().contains(memberid)) {
			freeCommentReply.getDdabong().remove(memberid);
		} else {
			freeCommentReply.getDdabong().add(memberid);
		}
    	
    	freeCommentReplyRepository.save(freeCommentReply);
    }
    
    public void nope(FreeCommentReply freeCommentReply, String memberid) {
    	
		if(freeCommentReply.getNope().contains(memberid)) {
			freeCommentReply.getNope().remove(memberid);
		} else {
			freeCommentReply.getNope().add(memberid);
		}
    	
    	freeCommentReplyRepository.save(freeCommentReply);
    }
    
    // 나의 추천수 총합 구하는 서비스구문
 	public int getMyDdabong(Member momoMember) {
 		List<FreeCommentReply> myFCR = this.freeCommentReplyRepository.findByAuthor(momoMember);
 		int totalDdabong = 0;
 		for(int i=0; i<myFCR.size(); i++) {
 			totalDdabong += myFCR.get(i).getDdabong().size();
 		}
 		return totalDdabong;
 	}
 	
 	// 나의 비추천수 총합 구하는 서비스구문
 	public int getMyNope(Member momoMember) {
 		List<FreeCommentReply> myFCR = this.freeCommentReplyRepository.findByAuthor(momoMember);
 		int totalNope = 0;
 		for(int i=0; i<myFCR.size(); i++) {
 			totalNope += myFCR.get(i).getNope().size();
 		}
 		return totalNope;
 	}
	
}

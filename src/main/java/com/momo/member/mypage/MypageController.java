package com.momo.member.mypage;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.board.ask.comment.AskComment;
import com.momo.board.ask.comment.AskCommentService;
import com.momo.board.ask.posting.AskPosting;
import com.momo.board.ask.posting.AskPostingService;
import com.momo.board.free.comment.FreeComment;
import com.momo.board.free.comment.FreeCommentService;
import com.momo.board.free.comment.re.FreeCommentReplyService;
import com.momo.board.free.posting.FreePosting;
import com.momo.board.free.posting.FreePostingService;
import com.momo.member.Member;
import com.momo.member.MemberService;
import com.momo.member.profile.ProfileService;
import com.momo.restaurant.et.EatTogether;
import com.momo.restaurant.et.EatTogetherService;
import com.momo.restaurant.jjim.Jjim;
import com.momo.restaurant.jjim.JjimService;
import com.momo.restaurant.review.Review;
import com.momo.restaurant.review.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

	
	private final FreePostingService freePostingService;
	private final AskPostingService askPostingService;
	private final MemberService memberService;
	private final JjimService jjimService;
	
	private final FreeCommentReplyService freeCommentReplyService;
	private final FreeCommentService freeCommentService;
	private final AskCommentService askCommentService;
	private final ReviewService reviewService;
	private final ProfileService profileService;
	private final EatTogetherService eatTogetherService;
	
	// 마이페이지 내 게시물 보기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myPosting")
	public String getMyPosting(Model model, @RequestParam(value="freeSubject", defaultValue="")String freeSubject, 
			                   @RequestParam(value="askSubject", defaultValue="")String askSubject
			                   , @RequestParam(value="page", defaultValue="0") int page
			                   , @RequestParam(value="pg", defaultValue="0") int pg 
			                   ,Principal principal) {
		Member member = this.memberService.getMember(principal.getName());
		Page<FreePosting> myFreePosting = this.freePostingService.getMyList(member, freeSubject, page);
		Page<AskPosting> myAskPosting = this.askPostingService.getMyList(member, askSubject, pg);
		model.addAttribute("myFreePosting", myFreePosting);
     	model.addAttribute("freeSubject", freeSubject );
		model.addAttribute("myAskPosting", myAskPosting);
		model.addAttribute("askSubject", askSubject);
		return "mypage/mypage_myPosting";
	}
	
	// 마이페이지 내 가게 찜 목록 보기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myJjim")
	public String getMyJjimList(Model model, @RequestParam(value="page", defaultValue="0")int page, Principal principal) {
		Member member = this.memberService.getMember(principal.getName());
		Page<Jjim> paging = this.jjimService.getMyJjimList(member, page);
		model.addAttribute("paging", paging);
		return "mypage/mypage_myJjim";
	}
	
	// 마이페이지 내 댓글(자유게시판, 질문&답변) 보기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myComment")
	public String getMyComment(Model model, @RequestParam(value="freeContent", defaultValue="")String freeContent, 
			                   @RequestParam(value="askContent", defaultValue="")String askContent
			                   , @RequestParam(value="page", defaultValue="0") int page
			                   , @RequestParam(value="pg", defaultValue="0") int pg 
			                   ,Principal principal) {
		Member member = this.memberService.getMember(principal.getName());
		Page<FreeComment> myFreeComment = this.freeCommentService.getMyFreeComment(member, freeContent, page);
		Page<AskComment> myAskComment = this.askCommentService.getMyAskComment(member, askContent, pg);
		model.addAttribute("myFreeComment", myFreeComment);
     	model.addAttribute("freeCotent", freeContent );
		model.addAttribute("myAskComment", myAskComment);
		model.addAttribute("askCotent", askContent);
		return "mypage/mypage_myComment";
	}
	
	// 마이페이지 내 리뷰 보기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myReview")
	public String getMyReview(Model model, @RequestParam(value="content", defaultValue="")String content, 
			                   @RequestParam(value="page", defaultValue="0")int page, Principal principal) {
		Member member = this.memberService.getMember(principal.getName());
		Page<Review> myReview = this.reviewService.getMyReview(member, content, page);
		model.addAttribute("myReview", myReview);
		model.addAttribute("content", content);
		return "mypage/mypage_myReview";
	}
	
	// 마이페이지 내 같이 먹기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myET")
	public String getMyEatTogether(Model model, @RequestParam(value="ettitle", defaultValue="")String ettitle,
			                       @RequestParam(value="page", defaultValue="0")int page, Principal principal) {
		
		Member member = this.memberService.getMember(principal.getName());
		Page<EatTogether> myET = this.eatTogetherService.getMyET(principal.getName(), page, ettitle);
		model.addAttribute("myET", myET );
		model.addAttribute("ettitle", ettitle);
		return "mypage/mypage_myET";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myRank")
	public String getMyRanking(Model model, Principal principal) {
		Member member = this.memberService.getMember(principal.getName());
		
		// 총 내가 받은 비추천수     
        int totalMyNope = this.askCommentService.getMyNope(member)
        		         + this.askPostingService.getMyNope(member)
        		         + this.freeCommentService.getMyNope(member)
        		         + this.freeCommentReplyService.getMyNope(member)
        		         + this.freeCommentService.getMyNope(member);
        
        // 총 내가 받은 추천수		
		int totalMyDdabong = this.reviewService.getMyDdabong(member)
				          + this.freePostingService.getMyDdabong(member) 
				          + this.freeCommentReplyService.getMyDdabong(member) 
				          + this.freeCommentService.getMyDdabong(member) 
				          + this.askPostingService.getMyDdabong(member)
				          + this.askCommentService.getMyDdabong(member);
		
        //총 내 같이먹기 횟수
		int myETcount = this.eatTogetherService.getMyETcount(member);
		
		//내 랭킹
		int myRank = this.profileService.getMyBrixRank(member);
		
		model.addAttribute("totalMyNope", totalMyNope);
		model.addAttribute("totalMyDdabong", totalMyDdabong);
		model.addAttribute("myETcount", myETcount);
		model.addAttribute("myRank", myRank);
		model.addAttribute("member", member);
		return "mypage/mypage_myRank";
	}
}

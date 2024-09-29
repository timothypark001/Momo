package com.momo.board.ask.comment;



import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.momo.board.ask.posting.AskPosting;
import com.momo.board.ask.posting.AskPostingService;
import com.momo.member.Member;
import com.momo.member.MemberService;
import com.momo.member.profile.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/askComment")
public class AskCommentController {
//aaaaaa
	private final AskCommentService askCommentService;
	private final AskPostingService askPostingService;
	private final MemberService momoMemberService;
	private final ProfileService profileService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{no}")
	public String createAskComment(Model model , @PathVariable("no") Integer no , @Valid AskCommentForm askCommentForm
			, BindingResult bindingResult , Principal principal) {
		AskPosting askPosting = this.askPostingService.getAskPosting(no);
		Member member = this.momoMemberService.getMember(principal.getName());
		if(bindingResult.hasErrors()) {
			model.addAttribute("member", member);
			model.addAttribute("askPosting", askPosting);
			return "ask/askPosting_detail";
		}
		model.addAttribute("member", member);
		model.addAttribute("askPosting", askPosting);
		AskComment askComment = this.askCommentService.create(askPosting, askCommentForm.getContent() , member);
		return String.format("redirect:/askPosting/detail/%s#askComment_%s", askComment.getAskPosting().getNo() , askComment.getNo());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{no}")
	public String updateAskComment(AskCommentForm askCommentForm , @PathVariable("no") Integer no , Principal principal) {
		AskComment askComment = this.askCommentService.getAskComment(no);
		if(!askComment.getAuthor().getMemberid().equals(principal.getName())) {
			if(!principal.getName().equals("admin")) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "수정 권한이 없습니다.");
			}
		}
		askCommentForm.setContent(askComment.getContent());
		return "ask/askComment_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{no}")
	public String updateAskComment(@Valid AskCommentForm askCommentForm , BindingResult bindingResult
			, @PathVariable("no") Integer no , Principal principal) {
		if(bindingResult.hasErrors()) {
			return "ask/askComment_form";
		}
		AskComment askComment = this.askCommentService.getAskComment(no);
		if(!askComment.getAuthor().getMemberid().equals(principal.getName())) {
			if(!principal.getName().equals("admin")) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "수정 권한이 없습니다.");
			}
		}
		this.askCommentService.update(askComment, askCommentForm.getContent());
		return String.format("redirect:/askPosting/detail/%s#askComment_%s" , askComment.getAskPosting().getNo() , askComment.getNo());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{no}")
	public String deleteAskComment(Principal principal , @PathVariable("no") Integer no) {
		AskComment askComment = this.askCommentService.getAskComment(no);
		if(!askComment.getAuthor().getMemberid().equals(principal.getName())) {
			if(!principal.getName().equals("admin")) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "삭제 권한이 없습니다.");
			}
		}
		this.askCommentService.delete(askComment);
		return String.format("redirect:/askPosting/detail/%s" , askComment.getAskPosting().getNo());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/voteDdabong/{no}")
	public String askCommentVote(Principal principal , @PathVariable("no") Integer no) {
		AskComment askComment = this.askCommentService.getAskComment(no);
		Member momoMember = this.momoMemberService.getMember(principal.getName());
		this.askCommentService.voteDdabong(askComment, momoMember);
		this.profileService.plusBrix(askComment.getAuthor());
		return String.format("redirect:/askPosting/detail/%s#askComment_%s", askComment.getAskPosting().getNo() , askComment.getNo());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/voteNope/{no}")
	public String askCommentNope(Principal principal , @PathVariable("no") Integer no) {
		AskComment askComment = this.askCommentService.getAskComment(no);
		Member momoMember = this.momoMemberService.getMember(principal.getName());
		this.askCommentService.voteNope(askComment, momoMember);
		this.profileService.minusBrix(askComment.getAuthor());
		return String.format("redirect:/askPosting/detail/%s#askComment_%s", askComment.getAskPosting().getNo() , askComment.getNo());
	}
}

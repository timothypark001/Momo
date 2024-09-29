package com.momo.board.inquiry.comment;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.momo.board.inquiry.posting.InquiryPosting;
import com.momo.board.inquiry.posting.InquiryPostingService;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiryComment")
public class InquiryCommentController {
	
	private final InquiryCommentService inquiryCommentService;
	private final InquiryPostingService inquiryPostingService;
	private final MemberService memberService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{no}")
	public String createComment(Model model, @PathVariable("no") Integer no, 
			     @Valid InquiryCommentForm inquiryCommentForm, BindingResult bindingResult, Principal principal) {
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(no);
		Member member = this.memberService.getMember(principal.getName());
		
		if(bindingResult.hasErrors()) {
		   model.addAttribute("posting", posting);
		   return "inquiry/inquiryPosting_detail";	
		}
		this.inquiryCommentService.create(posting, inquiryCommentForm.getContent(), member.getMembernick(), member);
		return String.format("redirect:/inquiryPosting/detail/%s", no);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{pno}/{cno}")
	public String deleteComment(@PathVariable(value="cno") Integer cno) {
		this.inquiryCommentService.delete(cno);
		return "redirect:/inquiryPosting/detail/{pno}";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{pno}/{cno}")
	public String updateComment(@PathVariable(value="cno") Integer cno, @PathVariable(value="pno") Integer pno,
			                     Model model, InquiryCommentForm inquiryCommentForm) {
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(pno);
		model.addAttribute("posting" ,posting);
		InquiryComment comment = this.inquiryCommentService.getComment(cno);
		inquiryCommentForm.setContent(comment.getContent());
		model.addAttribute("comment",comment);
		return "inquiry/inquiryComment_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{pno}/{cno}")
	public String updateComment(@PathVariable(value="cno") Integer cno, @PathVariable(value="pno") Integer pno
			                    , @Valid InquiryCommentForm inquiryCommentForm, BindingResult bindingResult, Model model) {
		
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(pno);
		InquiryComment comment = this.inquiryCommentService.getComment(cno);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("comment", comment);
			model.addAttribute("posting", posting);
			return "inquiry/inquiryComment_form";
		}
		this.inquiryCommentService.update(cno, inquiryCommentForm.getContent());
		return "redirect:/inquiryPosting/detail/{pno}";
	} 
	
	
	
}

package com.momo.board.inquiry.posting;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.board.inquiry.comment.InquiryCommentForm;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiryPosting")
public class InquiryPostingController {

	
	private final InquiryPostingService inquiryPostingService;
	private final MemberService memberService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/list")
	public String getPostingList(Model model, @RequestParam(value = "kw", defaultValue = "") String kw 
			                     , @RequestParam(value="page", defaultValue = "0")int page) {
		Page<InquiryPosting> paging = this.inquiryPostingService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "inquiry/inquiryPosting_admin";
	}  
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/noCommentList")
	public String getNoCommentList(Model model, @RequestParam(value="page", defaultValue = "0")int page,
			                       @RequestParam(value="subject", defaultValue="")String subject) {
		Page<InquiryPosting> paging = this.inquiryPostingService.getNoCommenList(subject, page);
		model.addAttribute("paging", paging); 
		model.addAttribute("subject",subject);
		return "inquiry/inquiryPosting_admin(noCommentList)";
	} 
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myList")
	public String getMyPosting(Model model, Principal principal, @RequestParam(value="subject", defaultValue="")String subject
			                   , @RequestParam(value="page", defaultValue = "0")int page) {
		Member member = this.memberService.getMember(principal.getName());
		Page<InquiryPosting> paging = this.inquiryPostingService.getMyList(member, subject ,page);
		model.addAttribute("paging", paging);
		model.addAttribute("subject", subject);
		return "inquiry/inquiryPosting";
	} 
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String createPosting(InquiryPostingForm inquiryPostingForm) {
		return "inquiry/inquiryPosting_create";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String createPosting(@Valid InquiryPostingForm inquiryPostingForm, BindingResult bindingResult, Principal principal, Model model
		                       , @RequestParam(value="page", defaultValue = "0")int page) {
		Member member = this.memberService.getMember(principal.getName());
		if(bindingResult.hasErrors()) {
			return "inquiry/inquiryPosting_create";
		}
		this.inquiryPostingService.createPosting(inquiryPostingForm.getSubject(), inquiryPostingForm.getContent(), member.getMembernick() , member);
		return "redirect:/inquiryPosting/myList";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/detail/{no}")
	public String detailPosting(Model model, @PathVariable(value="no") Integer no, Principal principal, 
			                    InquiryCommentForm inquiryCommentForm, InquiryPostingForm inquiryPostingForm) {
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(no);
		model.addAttribute("posting", posting);
		return "inquiry/inquiryPosting_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{no}")
	public String updatePosting(InquiryPostingForm inquiryPostingForm , @PathVariable(value="no")Integer no, Model model) {
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(no);
		
		inquiryPostingForm.setSubject(posting.getSubject());
		inquiryPostingForm.setContent(posting.getContent());
		
		model.addAttribute("posting", posting);
		
		return "inquiry/inquiryPosting_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{no}")
	public String updatePosting(@Valid InquiryPostingForm inquiryPostingForm, BindingResult bindingResult,
			                    @PathVariable(value="no")Integer no, Model model) {
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(no);
		if(bindingResult.hasErrors()) {
			model.addAttribute("posting", posting);
			return "inquiry/inquiryPosting_form";
		}
		this.inquiryPostingService.updatePosting(inquiryPostingForm.getSubject(), inquiryPostingForm.getContent(), no);
		return "redirect:/inquiryPosting/detail/{no}";
		
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{no}")
	public String deletePosting(@PathVariable(value="no")Integer no, Principal principal) {
		this.inquiryPostingService.deletePosting(no);

		if(principal.getName().contains("admin")) {
			return "redirect:/inquiryPosting/list";
		}
		return "redirect:/inquiryPosting/myList";
		
	}
	

}

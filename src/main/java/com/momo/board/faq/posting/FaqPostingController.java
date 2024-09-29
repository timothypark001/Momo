package com.momo.board.faq.posting;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.momo.board.faq.category.FaqCategory;
import com.momo.board.faq.category.FaqCategoryService;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FaqPostingController {

	private final FaqPostingService faqPostingService;
	private final FaqCategoryService faqCategoryService;
	private final MemberService memberService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/posting")
	public String createFaq(FaqPostingForm faqPostingForm, Model model) {
		List<FaqCategory> faqCategoryList=this.faqCategoryService.getFaqCategoryList();
		model.addAttribute("faqCategoryList", faqCategoryList);
		return "faq/faqPosting";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/posting")
	public String createFaq( 
			@Valid FaqPostingForm faqPostingForm, BindingResult bindingResult
			, Principal principal) {		
		Member member = this.memberService.getMember(principal.getName());
		this.faqPostingService.createFaq( faqPostingForm.getSubject(), faqPostingForm.getContent(), member);
		return "redirect:/faq/list";
	}	
	
	@GetMapping("/list")
	public String faqList(Model model, @RequestParam(value="page", defaultValue="0")int page
			, @RequestParam(value = "kw", defaultValue = "") String kw ) {
		Page<FaqPosting> paging = this.faqPostingService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		List<FaqCategory> faqCategoryList=this.faqCategoryService.getFaqCategoryList();
		model.addAttribute("faqCategoryList", faqCategoryList);
		return "faq/faqList";
	}	
	
	@GetMapping("/detail/{no}")
	public String faqDetail(Model model, @PathVariable("no") Integer no) {
		FaqPosting faq = this.faqPostingService.getfaqPosting(no);
		model.addAttribute("faq", faq);
		return "faq/faqDetail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/deleteFAQ/{no}")
	public String delete(@PathVariable("no") Integer no) {
		this.faqPostingService.delete(no);
		return "redirect:/faq/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/updateFAQ/{no}")
	public String update(@PathVariable("no") Integer no, FaqCategory faqCategory
			, FaqPostingForm faqPostingForm, Model model) {
		FaqPosting faq = this.faqPostingService.getfaqPosting(no);
		faqPostingForm.setSubject(faq.getSubject());
		faqPostingForm.setContent(faq.getContent());
		model.addAttribute(faq);
		return "faq/faqPosting";
	}
	

	
	
	
	
	
/*	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/updateFAQ/{no}")
	public String update(@PathVariable("no") Integer no, FaqCategory faqCategory, 
			@Valid FaqPostingForm faqPostingForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "faq/faqPosting";
        }
        FaqPosting faq = this.faqPostingService.getfaqPosting(no);
        if (!faq.getAuthor().getMemberid().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.faqPostingService.update(no, faqPostingForm.getSubject() , faqPostingForm.getContent());
        
		return String.format("redirect:/faq/detail/%s", no);
	}
	
	@GetMapping("/list") 
	public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
		Page<FaqPosting> paging = this.faqPostingService.getList(page);
		model.addAttribute("paging", paging);
		return "/faq/faqPosting_list";
	}
	*/
/*	
	
	@GetMapping("/list/{no}")
	public String faqList1(Model model, FaqCategory faqCategory
			
			,@PathVariable("no") Integer no) {
		Page<FaqPosting> paging = this.faqPostingService.getList(page);
		model.addAttribute("paging", paging); 
		
		
		
		List<FaqPosting> faqPostingList = this.faqPostingService.getList1(faqCategory);
		model.addAttribute("faqPostingList", faqPostingList);
		
		List<FaqCategory> faqCategoryList = this.faqCategoryService.getFaqCategoryList();
		model.addAttribute("faqCategoryList", faqCategoryList);
		
		return "faq/faqList1";
	}
*/	

	
}

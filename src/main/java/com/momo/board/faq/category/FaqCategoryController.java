package com.momo.board.faq.category;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/faq")
public class FaqCategoryController {

	private final FaqCategoryService faqCategoryService;
	private final MemberService memberService;
	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/category")
	public String createFaqCategory(FaqCategoryForm faqCategoryForm, Model model) {
		List<FaqCategory>faqCategoryList=this.faqCategoryService.getFaqCategoryList();
		model.addAttribute("faqCategoryList", faqCategoryList);
	
		return "faq/faqCategory";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/category")
	public String createFaqCategory(@Valid FaqCategoryForm faqCategoryForm, BindingResult bindingResult
					, Principal principal) {
		
		if(bindingResult.hasErrors()) {
			System.out.println("ERRORS");
			return "faq/faqCategory";
		}
		
		Member member = this.memberService.getMember(principal.getName());
		
		this.faqCategoryService.createFaqCategory(faqCategoryForm.getCategory(), member);
		return "redirect:/faq/category";
	}
	
	
	@GetMapping("delete/{no}")
	public String delete(@PathVariable("no") Integer no) {
		this.faqCategoryService.delete(no);
		return "redirect:/faq/category";
	}

	
	@GetMapping("update/{no}")
	public String update(@PathVariable("no") Integer no
					, @RequestParam(value="category") String category) {
				
		this.faqCategoryService.update(no, category);
		return "redirect:/faq/category";
	}
	
	
	
	
}

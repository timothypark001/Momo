package com.momo.board.free.posting;

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

import com.momo.board.free.comment.FreeComment;
import com.momo.board.free.comment.FreeCommentForm;
import com.momo.member.Member;
import com.momo.member.MemberService;
import com.momo.member.profile.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/free")
@RequiredArgsConstructor
public class FreePostingController {
	
	private final FreePostingService freePostingService;
	private final MemberService memberService;
	private final ProfileService profileService;
	
	@GetMapping("/list")
	public String getList(Model model, @RequestParam(value = "page", defaultValue = "0") int page
			, @RequestParam(required = false , value = "order" , defaultValue = "createDate") String order
			, @RequestParam(value = "kw" , defaultValue = "") String kw, Principal principal) {
		Page<FreePosting> paging = freePostingService.getList(page, order, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		if(principal != null) {
			Member member = memberService.getMember(principal.getName());
			model.addAttribute("member", member);
		}

		return "free/free_list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String create(FreePostingForm freePostingForm, Model model) {
		List<FreePosting> freePostingList = freePostingService.getList();
		
		for(int i=0 ; i < freePostingList.size() ; i++ ) {
			System.out.println(freePostingList.get(i).getSubject());
		}
		model.addAttribute("freePostingList", freePostingList);
		return "free/free_form";
	}
	
	@PostMapping("/create")
	public String create(@Valid FreePostingForm freePostingForm, BindingResult bindingResult, Principal principal) {
		
		if(bindingResult.hasErrors()) {
			return "free/free_form";
		}
		
		Member member = memberService.getMember(principal.getName());
		freePostingService.create(freePostingForm.getSubject(), member,
				member.getMembernick(), freePostingForm.getContent());
		return "redirect:/free/list";
	}
	
	@GetMapping("/update/{pno}")
	public String update(@PathVariable("pno") Integer pno, FreePostingForm freePostingForm) {
		FreePosting freePosting = freePostingService.getPosting(pno);
		
		freePostingForm.setSubject(freePosting.getSubject());
		freePostingForm.setContent(freePosting.getContent());
		
		return "free/free_form";
	}
	
	@PostMapping("/update/{pno}")
	public String update(@Valid FreePostingForm freePostingForm, BindingResult bindingResult,
			@PathVariable("pno") Integer pno) {
		
		if(bindingResult.hasErrors()) {
			return "free/free_form";
		}
		
		FreePosting freePosting = freePostingService.getPosting(pno);
		
		freePostingService.update(freePosting, freePostingForm.getSubject(), freePostingForm.getContent());
		
		return "redirect:/free/detail/{pno}";
	}
	
	
	@GetMapping("/detail/{pno}")
	public String detail(Model model, @PathVariable("pno") Integer pno, FreeCommentForm freeCommentForm, Principal principal) {
		
		if(principal == null) {
			FreePosting _freePosting = freePostingService.getPosting(pno);
			model.addAttribute("freePosting", _freePosting);
			return "free/free_detail";
		}

		FreePosting freePosting = freePostingService.getPosting(pno);
		model.addAttribute("freePosting", freePosting);
		Member member = memberService.getMember(principal.getName());
		model.addAttribute("member", member);
		return "free/free_detail"; 
	}
	
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/ddabong/{pno}")
    public String ddabong(Principal principal, @PathVariable("pno") Integer pno) {
        FreePosting freePosting = freePostingService.getPosting(pno);
        String _memberid = principal.getName();
        freePostingService.ddabong(freePosting, _memberid);
        profileService.plusBrix(freePosting.getAuthor());
        return "redirect:/free/detail/{pno}";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/nope/{pno}")
    public String nope(Principal principal, @PathVariable("pno") Integer pno) {
    	FreePosting freePosting = freePostingService.getPosting(pno);
    	String _memberid = principal.getName();
    	freePostingService.nope(freePosting, _memberid);
    	profileService.minusBrix(freePosting.getAuthor());
    	return "redirect:/free/detail/{pno}";
    }
    
    // 댓글 개수 입력을 위한 임시 메소드(추후에는 필요 없음)
    @GetMapping("/totalComment")
    public void calTotalComment() {
    	List<FreePosting> _list = freePostingService.getList();
    	
    	for(int i = 0 ; i < _list.size() ; i++) {
    		FreePosting _free = _list.get(i);
    		List<FreeComment> _commentList = _free.getFreeCommentList();
    		int totalC = _commentList.size();
    		int totalCR = 0;
    		
    		for(int j = 0 ; j < _commentList.size() ; j++) {
    			FreeComment _comment = _commentList.get(j);
    			totalCR += _comment.getFreeCommentReplyList().size();
    			
    			if(j == _commentList.size()-1) {
    			}
    		}
    		
    		totalC += totalCR;
    		_free.setTotalComment(totalC);
    		freePostingService.update(_free);
    		
    		if(i == _list.size()-1) {
    		}
    		
    	}
    }
    
    // 따봉 개수 입력을 위한 임시 메소드(추후에는 필요 없음)
    @GetMapping("/totalDdabong")
    public void calTotalDdabong() {
    	List<FreePosting> _list = freePostingService.getList();
    	
    	for(int i=0 ; i < _list.size() ; i++) {
    		FreePosting _freePosting = _list.get(i);
    		_freePosting.setDdabongCnt(_freePosting.getDdabong().size());
    		freePostingService.update(_freePosting);
    	}
    }
    
	@GetMapping("/delete/{pno}")
	public String delete(@PathVariable("pno") Integer pno) {
		freePostingService.delete(pno);
		return "redirect:/free/list";
	}
	
}

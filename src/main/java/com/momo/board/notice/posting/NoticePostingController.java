package com.momo.board.notice.posting;

import java.security.Principal;

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

import com.momo.board.notice.comment.NoticeCommentForm;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticePostingController {

	private final NoticePostingService noticePostingService; 
	private final MemberService memberService;
	
	//공지사항 목록
	@GetMapping("/list")
	public String postingList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<NoticePosting> paging = this.noticePostingService.getList(page,kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "notice/notice_list";
	}
	
	//공지사항 상세보기
	@GetMapping("/detail/{no}")
	public String detail(Model model,@PathVariable("no") Integer no, NoticeCommentForm noticeCommentForm ) {
		 NoticePosting noticePosting = this.noticePostingService.getNoticePosting(no);
		 model.addAttribute("noticePosting", noticePosting);
		 return "notice/notice_detail";
	}

	//공지 등록 
	@GetMapping("/create")
	public String noticeCreate(NoticePostingForm noticePostingForm) {
		return "notice/notice_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String noticeCreate(@Valid NoticePostingForm noticePostingForm, BindingResult bindingResult,Principal principal ) {
		Member member  = this.memberService.getMember(principal.getName());
		if(bindingResult.hasErrors()) {
			return "notice/notice_form";
		}
		
		this.noticePostingService.create(noticePostingForm.getSubject(), noticePostingForm.getContent(),member);
		return "redirect:/notice/list";		
	}
	
	//글 수정
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{no}")
	public String postingModify(NoticePostingForm noticePostingForm, @PathVariable("no") Integer no, Principal principal) {	
		 NoticePosting noticePosting = this.noticePostingService.getNoticePosting(no);
		if(!noticePosting.getAuthor().getMemberid().equals(principal.getName()) ) {  //로그인시 저장된 아이디와 글 번호에 해당하는 아이디가 불일치하면
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다");   //예외 처리
		}
		noticePostingForm.setSubject(noticePosting.getSubject()); //저장되어있는 걸로 가져오기
		noticePostingForm.setContent(noticePosting.getContent());
		return "notice/notice_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{no}")
	public String postingModify(@Valid NoticePostingForm noticePostingForm, BindingResult bindingResult, 
			Principal principal, @PathVariable("no")Integer no ) {
		if(bindingResult.hasErrors()) {
			return "notice_form";
		}
		NoticePosting noticePosting = this.noticePostingService.getNoticePosting(no); //글 번호
		if(!noticePosting.getAuthor().getMemberid().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다");
		}
		this.noticePostingService.modify(noticePosting,noticePostingForm.getSubject(), noticePostingForm.getContent());
		return String.format("redirect:/notice/detail/%s", no);
	}
	//공지 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{no}")
	public String noticeDelete(Principal principal, @PathVariable("no")Integer no) {
		
		NoticePosting noticePosting = this.noticePostingService.getNoticePosting(no);
		if(!noticePosting.getAuthor().getMemberid().equals(principal.getName()) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다");
		}
		this.noticePostingService.delete(noticePosting);
		return "redirect:/notice/list";
	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

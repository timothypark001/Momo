package com.momo.board.attend.comment;

import java.security.Principal;
import java.time.LocalDate;

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

import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("attendComment")
public class AttendCommentController 
{
	private final MemberService memberService;
	private final AttendCommentService attendCommentService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/list")
	public String getCommentList(Model model, @RequestParam(value="page", defaultValue = "0")int page, AttendCommentForm attendCommentForm, Principal principal) 
	{   	
		LocalDate today = LocalDate.now();
		
		AttendComment attend = this.attendCommentService.getAttend(principal.getName(), today);
		model.addAttribute("attend", attend);
		
		Page<AttendComment> paging = this.attendCommentService.getList(page, today);
		model.addAttribute("paging", paging);
		return "hello/attendCalendar_main";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String deleteComment(@PathVariable(value="id") Integer id)
	{
		this.attendCommentService.delete(id);
		return "redirect:/attendComment/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String createComment(@Valid AttendCommentForm attendCommentForm, BindingResult bindingResult, Principal principal, Model model, @RequestParam(value="page", defaultValue = "0")int page) 
	{
		LocalDate now = LocalDate.now();
		
		Member member = this.memberService.getMember(principal.getName());
		Page<AttendComment> paging = this.attendCommentService.getList(page, now);
		if(bindingResult.hasErrors()) 
		{
			model.addAttribute("paging",paging);
			return "hello/attendCalendar_main";
		}
		this.attendCommentService.create(attendCommentForm.getContent(), member.getMembernick(), member.getMemberid(), member);
		return "redirect:/attendComment/list";
	}

}

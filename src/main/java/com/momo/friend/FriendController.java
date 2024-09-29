package com.momo.friend;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.member.Member;
import com.momo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

	private final FriendService friendService; 
	private final MemberService memberService;

	//친구 목록
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/list")
	public String list(Principal principal, Model model) {
		 Member member = this.memberService.getMember(principal.getName());
		 model.addAttribute("member", member);
		return "friend/friend";
	}
	
	//친구 추가  
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create/{no}")
	public String createFriend(Principal principal, @PathVariable("no")Integer friendno, Model model) {	
		 Member friendMember  = this.memberService.getMember(friendno); 
		 Member myMember = memberService.getMember(principal.getName()); 
		 model.addAttribute("myMember", myMember); 
		friendService.createFriend(principal.getName(),friendMember ); 
		return "redirect:/friend/list";
	}
	
	//친구 삭제
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/delete")
	public String deleteFriend(Principal pricipal,@RequestParam(value = "friendid") String friendid) {
		Member myid = memberService.getMember(pricipal.getName());  
		Member myfriendid = memberService.getMember(friendid);  
		friendService.deleteFriend(myid, myfriendid);
		 return "redirect:/friend/list";
	}
	
	//친구 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{no}")
	public String deleteFriend(Principal pricipal, @PathVariable("no") Integer no) {
		Member myid = memberService.getMember(pricipal.getName());  
		Member myfriendid = memberService.getMember(no);
		friendService.deleteFriend(myid, myfriendid);
		return "redirect:/friend/list";
		
	}
	
	

	
}

 package com.momo.member.profile;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.member.Member;
import com.momo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class ProfileController {

	private final ProfileService profileService;
	private final MemberService memberService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		Member memberProfile = this.memberService.getMember(principal.getName());
		model.addAttribute("memberProfile", memberProfile);
		return "profile/profile";
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modifyProfile")
	public String modifyProfile(Model model, Principal principal) {
		Member memberProfile = this.memberService.getMember(principal.getName());
		model.addAttribute("memberProfile", memberProfile);
		return "profile/profile_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modifyProfile")
	public String modifyProfile(@RequestParam(value="gender") String gender, @RequestParam(value="mbti") String mbti, 
			                    @RequestParam(value="content") String content, Principal principal){
		
		Member member = this.memberService.getMember(principal.getName());
		
		this.profileService.modifyProfile(principal.getName(), gender, content, mbti);
		
		Profile profile = this.profileService.getProfile(member);
			
		this.memberService.updateMember(principal.getName(), profile);

		
		return "redirect:/mypage/profile";
	}
	


}

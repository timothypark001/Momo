package com.momo.ranking;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.momo.member.profile.Profile;
import com.momo.member.profile.ProfileService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingController {

	private final ProfileService profileService;
	
	
	
	@GetMapping("/rank")
	public String rankList(Model model) {
		List<Profile> profileList = this.profileService.getListProfile();
		model.addAttribute("profileList", profileList);
		return "ranking/ranking";
	}
}

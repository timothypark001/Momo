package com.momo.restaurant.review;

import java.security.Principal;
import java.time.LocalDate;
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
import com.momo.member.profile.ProfileService;
import com.momo.restaurant.RestService;
import com.momo.restaurant.Restaurant;
import com.momo.restaurant.et.EatTogether;
import com.momo.restaurant.jjim.Jjim;
import com.momo.restaurant.jjim.JjimService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rest/review")
public class ReviewController {

	private final ReviewService reviewService;
	private final RestService restService;
	private final MemberService memberService;
	private final ProfileService profileService;
	
	@GetMapping("/list/{no}")
	private String getList(@PathVariable("no")Integer no) {
		return null;
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{no}")
	public String create(@RequestParam(value="star") Integer star
			, @RequestParam(value="content") String content 
			, @PathVariable("no") Integer no, Principal principal
			) {
		Restaurant rest = restService.getRestaurant(no);
		Member member = memberService.getMember(principal.getName());
		reviewService.create(no, member, content, star);
		double avgStar = reviewService.avg(rest);
		restService.update(no, avgStar);
		
		return "redirect:/rest/detail/{no}";
	}
	
	@GetMapping("/delete/{no}/{rno}")
	public String delete(@PathVariable("rno") Integer rno
			, @PathVariable("no") Integer no) {
		reviewService.delete(rno);
		
		Restaurant rest = this.restService.getRestaurant(no);
		double avgStar = this.reviewService.avg(rest);
		this.restService.update(no, avgStar);
		return "redirect:/rest/detail/{no}";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/ddabong/{no}/{rno}")
	public String ddabong(Principal principal
			,@PathVariable("no") Integer no
			, @PathVariable("rno") Integer rno) {
		Review review = this.reviewService.getReview(rno);
		Member member = this.memberService.getMember(principal.getName());
		reviewService.ddabong(review, member);
		profileService.plusBrix(review.getAuthor());
		
		return "redirect:/rest/detail/{no}";
	}
	
	
	
	
	
}

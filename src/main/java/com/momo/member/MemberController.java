package com.momo.member;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.member.profile.Profile;
import com.momo.member.profile.ProfileService;
import com.momo.restaurant.RestService;
import com.momo.restaurant.Restaurant;
import com.momo.restaurant.et.EatTogether;
import com.momo.restaurant.et.EatTogetherService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final RestService restService;
	private final EatTogetherService etService;
	
	// 첫페이지(인덱스)
	@GetMapping("/welcome")
	public String welcome(Model model
			, @RequestParam(value="page", defaultValue = "0") int page
			,@RequestParam(value="kw" , defaultValue = "") String kw) {
		
		Page<Restaurant>paging = this.restService.getList(page);
		model.addAttribute("paging", paging); 
		Page<EatTogether> etpaging = this.etService.getListAll(page , kw);
		model.addAttribute("etpaging", etpaging);
		
		

		return "index";
	}
	
	// 회원가입 : 약관 동의 화면
	@GetMapping("/signup")
	public String signup(MemberCreateForm memberCreateForm) {
		return "member/signup_cou";
	}
	
	// 회원가입 : 정보 입력 폼
	@GetMapping("/signup_next")
	public String signupNext(MemberCreateForm memberCreateForm) {
		return "member/signup_form";
	}
	
	// 회원가입을 위한 메소드
	@PostMapping("/signup")
	public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "member/signup_form";
		}
		
		if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "member/signup_form";
		}
		
		try {
			memberService.create(memberCreateForm);
			
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "member/signup_form";
			
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "member/signup_form";
		}
		
		return "redirect:/";
	}
	
	// 로그인 화면으로 이동
	@GetMapping("/login")
	public String login() {
		return "member/login_form";
	}
	
	// 로그인 성공
	@GetMapping("/loginsuccessful")
	public String loginSuccessful(HttpSession session, Principal principal) {
		System.out.println("===========로그인 성공============");
		
		Member member = memberService.getMember(principal.getName());
		session.setAttribute("member", member);

		return "redirect:/member/welcome";
		
	}
	
	// 로그인 실패
	@GetMapping("/loginfailed")
	public String loginFailed(MemberCreateForm memberCreateForm) {
		return "redirect:/member/login";
	}
	
	// 아이디 찾기
	@GetMapping("/findid")
	public String findId() {
		return "member/find_id3";
	}
	
	@GetMapping("/findpw")
	public String findpw() {
		return "member/find_pw";
	}
	
	@GetMapping("/modifyMember")
	public String goToModify(Principal principal, Member member, Model model) {
		member = memberService.getMember(principal.getName());
		
		String _aid = member.getMemberid();
		String _aname = member.getMembername();
		String _amail = member.getEmail();
		
		int _mailAt = _amail.indexOf("@");
		int _mailShow = _amail.length() - _mailAt;
		
		_aid = _aid.substring(0, 2) + "*".repeat(_aid.length()-3) + _aid.substring(_aid.length()-1);
		_aname = _aname.substring(0, 1) + "*".repeat(_aname.length()-2) + _aname.substring(_aname.length()-1);
		_amail = _amail.substring(0, 2) + "*".repeat(_mailShow-2) + _amail.substring(_mailAt-1, _amail.length());

		member.setMemberid(_aid);
		member.setMembername(_aname);
		
		System.out.println("CreateDate : " + member.getCreateDate());

		model.addAttribute("member", member);
		
		return "mypage/mypage_check";
	}
	
	@PostMapping("/modifyMember")
	public String modifyMember(@RequestParam(value = "memberid") String memberid, @RequestParam(value = "membernick") String membernick
			, @RequestParam(value = "email") String email, Member member, Model model) {

		member = memberService.getMember(memberid);
		member.setMembernick(membernick);
		member.setEmail(email);
		
		memberService.updateMember(member);
		
		model.addAttribute("member", member);
		
		return "redirect:/member/modifyMember";
	}
	
	@GetMapping("/social")
	public String goToMypageSocial(Principal principal, Model model) {
		Member member  = memberService.getMember(principal.getName());
		
		if(member.getCreateDate() != null) {
			return "rediret:/member/modifyMember";
		} else {
			model.addAttribute("member", member);
			return "mypage/mypage_social";
		}
	}
	
	@PostMapping("/social")
	public String modifySocial(@RequestParam(value = "memberid") String memberid, @RequestParam(value = "membername") String membername
			, @RequestParam(value = "membernick") String membernick, @RequestParam(value = "modifyPw3") String password
			, @RequestParam(value = "mail") String email, Principal principal, Model model) {
		
		Member member = memberService.getMemberByEmail(email);
		
		member.setMemberid(memberid);
		member.setMembername(membername);
		member.setMembernick(membernick);
		member.setPassword(passwordEncoder.encode(password));
		member.setCreateDate(LocalDateTime.now());
		
		memberService.updateMember(member);
		
		model.addAttribute("member", member);
		
	    Authentication authentication = authenticationManager.authenticate
	    		(new UsernamePasswordAuthenticationToken(memberid, password));
	        SecurityContextHolder.getContext().setAuthentication(authentication);

		return "redirect:/member/modifyMember";

	}
	
	@PostMapping("/checkPw")
	public String checkPw(@RequestParam(value = "password") String password, Principal principal, Model model) {
		Member member = memberService.getMember(principal.getName());
		boolean result = memberService.checkPassword(member, password);
		
		if(result) {
			model.addAttribute("member", member);
			return "mypage/mypage_modify";
		} else {
			return "/";
		}
		
	}
	
	// 회원 탈퇴 페이지 이동
	@GetMapping("/drop")
	public String goToDrop() {
		return "member/drop_member";
	}
	
	// 회원 탈퇴 처리
	@PostMapping("/drop")
	public String goToDrop(@RequestParam(value = "memberid") String memberid, @RequestParam(value = "mail") String mail
			, @RequestParam(value = "password") String password, Principal principal) {
		
		Member _member = memberService.getMember(memberid);
		boolean pwCheckResult = false;
		
		if(principal.getName().equals(memberid)) {
			pwCheckResult = memberService.checkPassword(_member, password);
			
			if(pwCheckResult) {
				if(mail.equals(_member.getEmail())) {
					memberService.deleteMember(_member);
					return "redirect:/member/logout";
				} 
			} 
		} 
		
		return "redirect:/member/drop";
	}
	

	
	// 아래부터는 테스트용 메소드들입니다!!!!!!!!!!!!!!!!!!!!!!!
	@GetMapping("/test")
	public String goToTestPage() {
		return "member/mail_test";
	}

	
}

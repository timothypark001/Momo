package com.momo.mail;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.momo.member.Member;
import com.momo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

	private final MailService mailService;
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
    private int number; // 이메일 인증 숫자를 저장하는 변수

    // 테스트용 이메일 전송 : 본문 직접 작성용(get)
    @GetMapping("/test")
    public String MailPage() {
    	return "member/mail_test";
    }
    
    // 테스트용 이메일 전송 : 본문 직접 작성용(post)
    @PostMapping("/test")
    public String testMailSend(MailContent mailContent) {
    	mailService.mailTest(mailContent);
    	return "redirect:/mail/test";
    }
    
    //인증번호 이메일 전송 페이지로 이동(get)
    @GetMapping("/mail")
    public String mailPage2() {
    	return "member/mail_test2";
    }
    
    //인증번호 이메일 전송(post)
    @ResponseBody
    @PostMapping("/mailSend")
    public String authMailSend(@RequestParam(value = "mail") String mail, @RequestParam(value = "membername") String membername) {
    	int number = mailService.sendMail(mail, membername);
        String num = "" + number;
        return num;
    }
    
    @ResponseBody
    @PostMapping("/mailSendPw")
    public String authMailSendPw(@RequestParam(value ="mail") String mail, @RequestParam(value = "membername") String membername
    		, @RequestParam(value = "memberid") String memberid) {
    	int number = mailService.sendMailPw(mail, membername, memberid);
    	String num = "" + number;
    	
    	return num;
    }
    
    
    // JSON 테스트용
    @GetMapping("/jsontest")
    public String jsonTest() {
    	return "member/mail_test3";
    }


    // JSON 전용 인증번호 확인 후 아이디 반환
    @ResponseBody
    @PostMapping("/checkCode")
    public MailReturn checkCode(@RequestParam(value ="mail") String mail) {
    	String memberid = mailService.checkCode(mail).getMemberid();
    	MailReturn mailReturn = new MailReturn();
    	mailReturn.setMemberid(memberid);
    	
    	System.out.println("확인된 아이디 : " + memberid);
    	
    	return mailReturn;
    }
    
    // JSON 전용 인증번호 확인 후 비밀번호 재설정
    @ResponseBody
    @PostMapping("/resetPw")
    public void resetPw(@RequestParam(value = "memberid") String memberid, @RequestParam(value = "password") String password) {
    	System.out.println("확인된 아이디 : " + memberid);
    	System.out.println("확인된 패스워드 : " + password);
    	
    	Member member = memberService.getMember(memberid);
    	member.setPassword(passwordEncoder.encode(password));
    	
    	memberService.updateMember(member);
    	
    }




    /*
    // 테스트용 인증번호 확인(String 리턴)
    @ResponseBody
    @PostMapping("/checkCode")
    public String checkCode(@RequestParam(value ="mail") String mail) {
    	String memberid = mailService.checkCode(mail).getMemberid();
    	
    	System.out.println("확인된 아이디 : " + memberid);
    	
    	return memberid;
    }
    
    

	// 인증 이메일 전송
	@PostMapping("/send")
    public HashMap<String, Object> mailSend(String mail) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            number = mailService.sendMail(mail);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

	// 인증번호 일치여부 확인
    @GetMapping("/check")
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber) {

        boolean isMatch = userNumber.equals(String.valueOf(number));

        return ResponseEntity.ok(isMatch);
    }
    */
}

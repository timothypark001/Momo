package com.momo.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender javaMailSender;
	private final MemberService memberService;
    private static final String senderEmail= "momo2gather@gmail.com";
    private static int number;

	// 랜덤으로 숫자 생성
    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }
    
    // 일반 메일 작성
    public void mailTest(MailContent mailContent) {
    	
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailContent.getMailAddress());
        message.setFrom(senderEmail);
        message.setSubject(mailContent.getSubject());
        message.setText(mailContent.getContent());

        javaMailSender.send(message);
    }
    
    public MimeMessage CreateMail(String mail) {
    	createNumber();
    	MimeMessage message = javaMailSender.createMimeMessage();

    	try {
    		message.setFrom(senderEmail);
    		message.setRecipients(MimeMessage.RecipientType.TO, mail);
    		message.setSubject("[모두모이자, 모모] 이메일 인증");
    		String body = "";
    		body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
    		body += "<h1>" + number + "</h1>";
    		body += "<h3>" + "감사합니다." + "</h3>";
    		message.setText(body,"UTF-8", "html");
    	} catch (MessagingException e) {
    		e.printStackTrace();
    	}

    	return message;
    }
    
    // 아이디 찾기용 sendMail
    public int sendMail(String mail, String membername) {
        MimeMessage message = CreateMail(mail);
        
        Member member = memberService.getMemberByEmail(mail);
        System.out.println(member.toString());
        
        if(membername.equals(member.getMembername())) {
        	javaMailSender.send(message);
        } else {
        	throw new DataNotFoundException("일치하는 회원을 찾을 수 없습니다");
        }
        

        return number;
    }
    
    // 비밀번호 찾기용 sendMailPw
    public int sendMailPw(String mail, String membername, String memberid) {
    	MimeMessage message = CreateMail(mail);
    	
    	Member member = memberService.getMemberByEmail(mail);
    	System.out.println(member.toString());
    	
    	if(membername.equals(member.getMembername())) {
    		
    		if(memberid.equals(member.getMemberid())) {
    			javaMailSender.send(message);
    			
    		} else {
    			throw new DataNotFoundException("일치하는 회원을 찾을 수 없습니다");
    		}
    		
    	} else {
    		throw new DataNotFoundException("일치하는 회원을 찾을 수 없습니다");
    	}
    	
    	
    	return number;
    }
    
    
    /*
    public int sendMail(String mail) {
        MimeMessage message = CreateMail(mail);
        
        Member member = memberService.getMemberByEmail(mail);
        System.out.println(member.toString());
        
        if(membername.equals(member.getMembername())) {
        	javaMailSender.send(message);
        } else {
        	throw new DataNotFoundException("일치하는 회원을 찾을 수 없습니다");
        }
        

        return number;
    }
    */
    
    public Member checkCode(String email) {
    	Member member = memberService.getMemberByEmail(email);
    	return member;
    }
    
    

}

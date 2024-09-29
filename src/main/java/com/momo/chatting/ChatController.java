package com.momo.chatting;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.momo.member.Member;
import com.momo.member.MemberService;
import com.momo.restaurant.et.EatTogether;
import com.momo.restaurant.et.EatTogetherService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;
	private final MemberService memberService;
	private final EatTogetherService eatTogetherService; 

	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/chat/list/{no}")
	//대화방에 같이먹기 신청자 목록 출력
	public String get(@PathVariable("no")Integer no, Model model,Principal principal) {
	
		System.out.println("채팅 화면 초기 진입 확인");
		
	
	EatTogether eatTogether = eatTogetherService.getET(no);  //같이먹기 고유번호를 객체로
	
	List<Member> etMemberList	= eatTogether.getPrtmember(); //객체의 같이먹기 리스트를 검색
	//검색한 리스트에서 로그인한 정보가 있는지 확인
	Member me = memberService.getMember(principal.getName());
	etMemberList.remove(me);
	model.addAttribute("etMemberList", etMemberList); //모델에 저장 
	model.addAttribute("me", me);
	return "chat/chatting";
		
	}
	

	@MessageMapping("/message.sendMessage")
	@SendTo("/topic/message")
	public Message sendMessage(Message message, Principal principal) {
		//보내는 사람 = 로그인 한 사람
		Member sender = this.memberService.getMember(principal.getName()); 
		//받는 사람
		Member receiver = this.memberService.getMember(message.getChatroom().getMember2().getMemberid());
		//메세지
		String content = message.getContent();
		return chatService.saveMessage(sender, receiver, content);
	}
	
	
	//기존 대화방, 대화내용 불러오기
	@GetMapping("/chat/message/{member2}")
	@ResponseBody
	public List<Message> getMessage(@PathVariable("member2")String member2,Principal principal){
		List<Message> mList = this.chatService.getMessage(principal.getName(),member2);	
		return mList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}

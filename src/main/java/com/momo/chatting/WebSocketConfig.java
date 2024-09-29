package com.momo.chatting;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//stomp 접속 url -> /ws/chat
		registry.addEndpoint("/ws")   //연결될 엔드 포인트
		.setAllowedOriginPatterns("*")
		.withSockJS();   //SocketJS 를 연결한다는 설정
	}
	
	
	@Override
    public void configureMessageBroker(MessageBrokerRegistry registry ) {
		//메세지를 구독하는 요청 url -> 메세지 받을 때
        registry.enableSimpleBroker("/topic");
        //메세지를 발행하는 요청 url -> 메세지를 보낼 때
        registry.setApplicationDestinationPrefixes("/app");
    }
	
	
	
	
	
}

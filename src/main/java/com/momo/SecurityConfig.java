package com.momo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.UserInfoEndpointConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.momo.auth.OAuth2UserCustomService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	private final OAuth2UserCustomService oAuth2UserCustomService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAnyAuthority("ROLE_ADMIN")
				.requestMatchers(new AntPathRequestMatcher("/member/mypage/**")).hasAnyRole("ADMIN", "MEMBER", "SOCIAL")
				.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
//				.csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
				.csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/mail/**")))
				.headers((headers) -> headers.addHeaderWriter(
						new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
				.formLogin((formLogin) -> formLogin
				.usernameParameter("memberid")
				.loginPage("/member/login")
				.failureUrl("/member/loginfailed")
				.defaultSuccessUrl("/member/loginsuccessful"))
				.logout((logout) -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true));

		http.oauth2Login((oauth2) -> oauth2
				.loginPage("/member/login")
				.userInfoEndpoint
				((userInfoEndpointConfig) -> userInfoEndpointConfig.userService(oAuth2UserCustomService))
				.defaultSuccessUrl("/member/modifyMember"));

        http.authorizeHttpRequests((auth) -> auth
        		.requestMatchers("/member/mypage/**").hasAnyRole("ADMIN", "MEMBER", "SOCIAL")
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated());
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}

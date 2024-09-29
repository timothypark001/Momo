package com.momo.member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.momo.auth.OAuth2Member;
import com.momo.image.Image;
import com.momo.member.profile.Profile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Member implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Integer no;
	
	private String memberid;
	
	private String membernick;
	
	private String membername;
	
	@JsonIgnore
	private String password;
	
	private String email;
	
	@JsonIgnore
	private LocalDateTime createDate;
	
	@ManyToMany
	@JsonIgnore
	private List<Member> friend;
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<OAuth2Member> oauth2MemberList;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "image_no")
	@JsonIgnore
	private Image image;
	
	@OneToOne
	@JoinColumn(name = "profile_no")
	@JsonIgnore
	private Profile profile;

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		if ("admin".equals(memberid)) {
			authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(MemberRole.MEMBER.getValue()));
		}
		
		return authorities;
	}

	@Override
	public String getUsername() {
		return this.memberid;
	}
	
	

}

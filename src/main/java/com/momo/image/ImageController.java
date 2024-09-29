package com.momo.image;

import java.io.IOException;
import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.momo.member.Member;
import com.momo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

	private final ImageService imageService;
	private final MemberService memberService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/upload")
	public String uploadImage(@RequestPart(value="files") MultipartFile file, Principal principal) throws IOException {
		Member member = this.memberService.getMember(principal.getName());
		Image image = this.imageService.storeImage(file, member);
		
		//확장자 검사
		String ext = null;
		if(file.isEmpty()) {
			ext = this.imageService.extension(image.getOriginalFilename());
		} else {
			ext = this.imageService.extension(file.getOriginalFilename());
		}
				
		if(ext.equals(".jpg") || ext.equals(".png") || ext.equals(".jpeg")) {
		
		Image img = this.imageService.getImage(principal.getName());
		
		
		
		if(img == null) {	
		  this.imageService.saveImage(image);
		} else {
		  this.imageService.updateImage(image, member);
		}
		this.memberService.updateMember(principal.getName(), image);
		} else {
			return "redirect:/mypage/profile";
		}
		
		return "redirect:/mypage/profile";
	
	}
	
}



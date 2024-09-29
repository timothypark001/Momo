package com.momo.image;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.momo.DataNotFoundException;
import com.momo.member.Member;
import com.momo.member.MemberRepository;
import com.momo.member.profile.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	
	private final ImageRepository imageRepository;
	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;
	
	public String extractExt(String originalFilename) {
		int pos = originalFilename.lastIndexOf(".");
		return originalFilename.substring(pos + 1);
	}
	
	//확장자 얻기 메소드 ex).jpg / .jfif / .png
	public String extension(String originalFilename) {
		int pos = originalFilename.lastIndexOf(".");
		return originalFilename.substring(pos);
	}
	
	
	public Image storeImage(MultipartFile file, Member member) throws IOException {

        // 프로필 이미지를 넣지않고 저장시 기본 이미지 들어가기
        if(file.isEmpty()) {
            String originalFilename = "default_profile.png";
            String storeFilename = "default_profile.png";
            
            Optional<Image> _image = this.imageRepository.findByAuthor(member);
            if(_image.isPresent()) {
             Image image = _image.get();
             image.setOriginalFilename(originalFilename);
             image.setStoreFilename(storeFilename);
             return image;
            } else {
             Image image = new Image();
             image.setOriginalFilename(originalFilename);
             image.setStoreFilename(storeFilename);
             image.setAuthor(member);
             return image;
            }
        }
        
        String originalFilename = file.getOriginalFilename();
        String storeFilename = UUID.randomUUID() + "." + extractExt(originalFilename);
//        String myDirectory = System.getProperty("user.dir");
        String fullPath = "/home/ubuntu/teammomo/images/" + storeFilename;
        file.transferTo(new File(fullPath));

          Optional<Image> _image = this.imageRepository.findByAuthor(member);
          if(_image.isPresent()) {
            Image image = _image.get();
            image.setOriginalFilename(originalFilename);
            image.setStoreFilename(storeFilename);
            return image;
        } else {
            Image image = new Image();
            image.setOriginalFilename(originalFilename);
            image.setStoreFilename(storeFilename);
            image.setAuthor(member);
            return image;
        }
    }
	
	 public Image getImage(String memberid) {
			Optional<Member> _member = this.memberRepository.findBymemberid(memberid);
			Member member = new Member();
			if(_member.isPresent()) {
				member = _member.get();
			} else {
				throw new DataNotFoundException("유저가 없습니다");
			}
	    	Optional<Image> image = this.imageRepository.findByAuthor(member);

	    	if(image.isPresent()) {
	    		return image.get();
	    	} else {
	    		return null;
	    	}
	    }  
	    
	    public void saveImage(Image image) {
	    	this.imageRepository.save(image);
	    }
	    
	  public void updateImage(Image image, Member member) { 
		  Optional<Image> _image = this.imageRepository.findByAuthor(member);
		  if(_image.isPresent()) {
			  Image img = _image.get();
			  img.setOriginalFilename(image.getOriginalFilename());
			  img.setStoreFilename(image.getStoreFilename());
			  this.imageRepository.save(img);
		  } else {
			  throw new DataNotFoundException("해당 회원의 이미지를 찾을 수 없습니다");
		  }
		  
	  }
	  
}

package com.momo.board.notice.posting;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticePostingForm {   //유효성 검사
	
	
	
	
	
	
	@NotEmpty(message = "제목을 반드시 입력해 주세요")
	@Size(max = 200)
	public String subject;
	
	@NotEmpty(message = "내용을 반드시 입력해 주세요 ")
	public String content;
	
	
	
	
}

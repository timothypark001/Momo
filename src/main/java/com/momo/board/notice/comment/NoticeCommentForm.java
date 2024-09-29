package com.momo.board.notice.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeCommentForm {

	
	
	@NotEmpty(message = "내용을 반드시 입력해 주세요")
	public String content;
	
	

}



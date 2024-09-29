package com.momo.board.inquiry.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryCommentForm {

	
	@NotEmpty(message = "내용 필수")
	private String content;
}

package com.momo.board.inquiry.posting;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryPostingForm {

	
	@NotEmpty(message = "제목 필수")
	private String subject;
	
	@NotEmpty(message = "내용 필수")
	private String content;
}

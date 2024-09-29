package com.momo.board.ask.posting;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AskPostingForm {

	@NotEmpty(message = "제목을 작성해주세요")
	@Size(max = 100)
	private String subject;
	
	@NotEmpty(message = "내용을 작성해주세요")
	private String content;
}

package com.momo.board.free.posting;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FreePostingForm {

	@NotEmpty(message = "제목은 필수 항목입니다")
	private String subject;
	
	@NotEmpty(message = "내용은 필수 항목입니다")
	private String content;
}

package com.momo.board.free.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FreeCommentForm {
	
	@NotEmpty(message = "내용은 필수 항목입니다")
	private String content;

}

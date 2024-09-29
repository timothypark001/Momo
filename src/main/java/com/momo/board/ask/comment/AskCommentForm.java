package com.momo.board.ask.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AskCommentForm {

	@NotEmpty(message = "댓글내용을 작성해주세요")
	private String content;
}

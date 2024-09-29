package com.momo.board.attend.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendCommentForm {

	
	@NotEmpty(message = "내용 필수")
	private String content;
}

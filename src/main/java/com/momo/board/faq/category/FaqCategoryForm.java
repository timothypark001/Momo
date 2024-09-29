package com.momo.board.faq.category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqCategoryForm {

	
	@NotNull(message = "카테고리 선정은 필수입니다")
	private String category;
	
	
}

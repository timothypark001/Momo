package com.momo.restaurant.et;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EatTogetherForm {

	@NotEmpty(message = "제목을 작성해주세요")
	@Size(max = 18)
	private String ettitle;
	
	@NotNull(message = "날짜를 선택해주세요")
	@Future(message = "현재보다 이후의 시간을 선택해주세요")
	private LocalDateTime etdate;
	
	@NotEmpty(message = "인원수를 선택해주세요")
	private String prtnumber;
	
	@NotEmpty(message = "선호 성별을 선택해주세요")
	private String prefgender;
	
	@NotEmpty(message = "선호 성격유형을 선택해주세요")
	private String prefmbti;
}

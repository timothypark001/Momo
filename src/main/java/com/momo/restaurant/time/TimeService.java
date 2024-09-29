package com.momo.restaurant.time;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.restaurant.RestRepository;
import com.momo.restaurant.Restaurant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimeService {

	private final TimeRepository timeRepository;

	public List<RestTime> getList(Restaurant rest){
		return this.timeRepository.findByRest(rest);
	}

	public List<RestTime> timeList(Integer no){
		List<RestTime>timeList = this.timeRepository.findAll();
		return timeList;
	}

	public RestTime getTime(Integer no) {
		Optional<RestTime> time = this.timeRepository.findById(no);
		if(time.isPresent()) {
			return time.get();
		}else {
			throw new DataNotFoundException("데이터가 없습니다");
		}	
	}
}

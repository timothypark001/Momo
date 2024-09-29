package com.momo.restaurant.jjim;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.momo.member.Member;
import com.momo.restaurant.Restaurant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JjimService {

	private final JjimRepository jjimRepository;
	
	public Jjim get(Member member, Restaurant rest) {	
		Optional<Jjim> _jjim	=  this.jjimRepository.findByMemberAndRest(member, rest);   
		if(_jjim.isPresent()) {System.out.println("찜 있음");
	   		return _jjim.get();
	   		
	    }else{
	    	System.out.println("찜 없음");
	    	return null;
	    }
	}
	
	public void create(Member member, Restaurant rest) {
		Jjim jjim = new Jjim();
		jjim.setMember(member);
		jjim.setRest(rest);
		this.jjimRepository.save(jjim);	
	}
	
	public void delete(Member member, Restaurant rest) {
		Jjim jjim = this.get(member, rest);
		this.jjimRepository.delete(jjim);
	}
	
	public List<Jjim> getList(Restaurant rest){
		return this.jjimRepository.findByRest(rest);
		
	}
	
	//마이페이지에서 내 찜 리스트 가져오기
	public Page<Jjim> getMyJjimList(Member member, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("no"));
		Pageable pageable = PageRequest.of(page, 6, Sort.by(sorts));
		return this.jjimRepository.findByMember(member, pageable);
		
	}
}

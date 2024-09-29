package com.momo.restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.board.faq.posting.FaqPosting;
import com.momo.member.Member;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestService {

	private final RestRepository restRepository;
	
	public Restaurant getRestaurant(Integer no) {
		Optional<Restaurant> rest = this.restRepository.findById(no);
		
		if(rest.isPresent()) {
			return rest.get();
		}else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
		
	}
	//인덱스 화면의 식당리스트
	public Page<Restaurant> getList( int page){	
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		sorts.add(Sort.Order.desc("no"));
		Pageable pageable = PageRequest.of(page ,  3 , Sort.by(sorts));
		return this.restRepository.findAll(pageable);
	} 
	//카테고리별 가게리스트
	public Page<Restaurant> getList(String category, int page){	
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		sorts.add(Sort.Order.asc("name"));
		Pageable pageable = PageRequest.of(page ,  9 , Sort.by(sorts));
		return this.restRepository.findByCategory(category, pageable);	
	} 
	//검색용 리스트
	public List<Restaurant>getList(String kw){
		Specification<Restaurant> spec = search(kw);
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		sorts.add(Sort.Order.desc("no"));
		return this.restRepository.findAll(spec);
		
	}	
	public Page<Restaurant>getListSearch(String kw, int page){
		Specification<Restaurant> spec = search(kw);
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		sorts.add(Sort.Order.asc("name"));
		Pageable pageable = PageRequest.of(page ,  9 , Sort.by(sorts));	
		return this.restRepository.findAll(spec, pageable);
	}
	
	
	
	private Specification<Restaurant> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Restaurant> r, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거 
                return cb.or(cb.like(r.get("name"), "%" + kw + "%"), 
                        cb.like(r.get("addr"), "%" + kw + "%"),      
                        cb.like(r.get("menu"), "%" + kw + "%"),		
                        cb.like(r.get("category"), "%" + kw + "%"));      
                      
            }
        };
    }

	// 가게 평점 업데이트
	public void update(Integer no, double avg) {
		Optional<Restaurant> _rest = this.restRepository.findById(no);
		Restaurant rest = new Restaurant();
		if(_rest.isPresent()) {
			rest = _rest.get();
		} else {
			throw new DataNotFoundException("식당을 찾을 수 없습니다");
		}
		
		double avgStar = Math.round(avg * 10) / 10.0;
		rest.setAvgStar(avgStar);
		this.restRepository.save(rest);
	}

}

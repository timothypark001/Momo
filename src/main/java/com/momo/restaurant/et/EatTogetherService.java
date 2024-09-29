package com.momo.restaurant.et;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.member.Member;
import com.momo.restaurant.Restaurant;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EatTogetherService {

	private final EatTogetherRepository etRepository;
	
	//같이먹기 등록 서비스구문
	public void create(Restaurant rest , Member applymember , String ettitle , LocalDateTime etdate 
			, String prtnumber , String prefgender , String prefmbti) {
		EatTogether et = new EatTogether();
		List<Member> list = new ArrayList<>();
		list.add(applymember);
		et.setPrtmember(list);
		et.setRest(rest);
		et.setApplymember(applymember);
		et.setEttitle(ettitle);
		et.setRegdate(LocalDateTime.now());
		et.setEtdate(etdate);
		et.setPrtnumber(prtnumber);
		et.setPrefgender(prefgender);
		et.setPrefmbti(prefmbti);
		this.etRepository.save(et);
		System.out.println("같이먹기 등록 데이터입력완료");
	}
	
	public Page<EatTogether> getListAll(int page , String kw){
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		sorts.add(Sort.Order.desc("regdate"));
		Pageable pageable = PageRequest.of(page, 3 , Sort.by(sorts));
		Specification<EatTogether> spec = search(kw);
		
		return this.etRepository.findAll(spec , pageable);
	}
	
	//같이먹기 전체 리스트 등록날짜의 내림차순으로 출력하기
	public Page<EatTogether> getListAll(int page , String option , String kw){
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		sorts.add(Sort.Order.desc("regdate"));
		Pageable pageable = PageRequest.of(page, 6 , Sort.by(sorts));
		
		if(option.equals("ettitle")) {
			return this.etRepository.findEttitleByKeyword(kw, pageable);
		}else if(option.equals("name")) {
			return this.etRepository.findNameByKeyword(kw, pageable);
		}else if(option.equals("prefmbti")) {
			return this.etRepository.findPrefmbtiByKeyword(kw, pageable);
		}else if(option.equals("prefgender")) {
			return this.etRepository.findPrefgenderByKeyword(kw, pageable);
		}else {
		
		Specification<EatTogether> spec = search(kw);
		
		return this.etRepository.findAll(spec , pageable);
		}
	}
	
	//가게 정보로 같이먹기 리스트 등록날짜의 내림차순으로 출력하기
	public List<EatTogether> getList(Restaurant rest) {
		
		return this.etRepository.findByRestOrderByDesc(rest);
	}
	
	//마이페이지 내 같이 먹기 + 페이징 + 검색
	public Page<EatTogether> getMyET(String memberid, int page, String ettitle) {
		List<EatTogether> _etList = this.etRepository.findAllOrderByDesc(ettitle);
		List<EatTogether> _myEtList = new ArrayList<>();
		for(int i=0; i<_etList.size(); i++) {
			for(int j=0; j<_etList.get(i).getPrtmember().size(); j++) {
				if(_etList.get(i).getPrtmember().get(j).getMemberid().equals(memberid)) {
					_myEtList.add(_etList.get(i));
				}
			}
		}
		PageRequest pageRequest = PageRequest.of(page, 6);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), _myEtList.size());
		Page<EatTogether> myEtPage = new PageImpl<>(_myEtList.subList(start, end), pageRequest, _myEtList.size());
	    return myEtPage;
	}
	
	//같이먹기 참여 서비스구문
	public void participate(EatTogether et , Member momoMember) {
		
		et.getPrtmember().add(momoMember);
		this.etRepository.save(et);
	}
	
	//같이먹기 조회
	public EatTogether getET(Integer no) {
		Optional<EatTogether> et = this.etRepository.findById(no);
		if(et.isPresent()) {
			return et.get();
		}else {
			throw new DataNotFoundException("No Data");
		}
	}
	
	//같이먹기 삭제
	public void delete(EatTogether et) {
		this.etRepository.delete(et);
	}
	
	//같이먹기 나가기
	public void leave(EatTogether et , Member momoMember) {
		et.getPrtmember().remove(momoMember);
		this.etRepository.save(et);
	}
	
	// 내가 같이 먹은 횟수 구하는 서비스구문 
		public int getMyETcount(Member momoMember) {
			LocalDateTime today = LocalDateTime.now();
			List<EatTogether> ETList = this.etRepository.findAll();
	        int myETcount = 0;
			for(int i=0; i<ETList.size(); i++) {
				for(int j=0; j<ETList.get(i).getPrtmember().size(); j++) {
					if(ETList.get(i).getEtdate().isBefore(today)) {
					if(ETList.get(i).getPrtmember().get(j).getMemberid().equals(momoMember.getMemberid())) {
						++myETcount;
					}
					
					} else {
						continue;
					}
				}
			}
			return myETcount;
		} 
	
	//같이먹기 검색 키워드 조인
	private Specification<EatTogether> search(String kw){
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<EatTogether> et , CriteriaQuery<?> query
					, CriteriaBuilder cb) {
				query.distinct(true);
				Join<EatTogether , Member> m = et.join("prtmember" , JoinType.LEFT);
				Join<Restaurant , EatTogether> r = et.join("rest" , JoinType.LEFT);
				
				return cb.or(cb.like(et.get("ettitle"), "%" + kw + "%"),
						cb.like(et.get("prefmbti"), "%" + kw + "%" ),
						cb.like(r.get("name"), "%" + kw + "%"),
						cb.like(m.get("membernick"), "%" + kw + "%"));
			
			}
		};
	}
}

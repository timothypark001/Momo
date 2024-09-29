package com.momo.restaurant.review;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.member.Member;
import com.momo.restaurant.RestRepository;
import com.momo.restaurant.Restaurant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final RestRepository restRepository;
	
	public List<Review> getList(Restaurant rest){
		return this.reviewRepository.findByRest(rest);
	}
	
	public List<Review> reviewList(Integer no){
		List<Review> reviewList = reviewRepository.findAll();
		return reviewList;
	}
	
	public Review getReview(Integer no) {
		
		Optional<Review> re = reviewRepository.findById(no);
		if(re.isPresent()) {
			return re.get();
		}else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
	}

	public void create(Integer no, Member member, String content, Integer star) {
		Restaurant rest = new Restaurant();
		Optional<Restaurant> findRest = restRepository.findById(no);
		rest = findRest.get();
		Review review = new Review();
		review.setStar(star);
		review.setAuthor(member);
		review.setRest(rest);
		review.setContent(content);
		review.setMembernick(member.getMembernick());
		review.setCreateDate(LocalDateTime.now());
		reviewRepository.save(review);
	}
	
	public void delete(Integer no) {
		reviewRepository.deleteById(no);
	}

	public void ddabong(Review review, Member member) {
		review.getDdabong().add(member);
		reviewRepository.save(review);
	}
	
	
	//마이페이지 내 리뷰 + 검색 + 페이징
	public Page<Review> getMyReview(Member member, String content, int page) {
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.desc("createDate"));
			Pageable pageable = PageRequest.of(page, 3, Sort.by(sorts));
			return this.reviewRepository.findByAuthorAndContent(member, content, pageable);
	}
	// 가게 평점 계산 메소드
	// 해당 가게에 회원이 등록한 점수를 토대로 반영
	public double avg(Restaurant rest) {
		List<Review> reviewList = this.reviewRepository.findByRest(rest);
		double avgStar = 0.0d;
		int sum = 0;
		for(int i = 0; i<reviewList.size(); i++) {
			sum += reviewList.get(i).getStar();
		}
		double size = reviewList.size();
		avgStar = sum / size;
		return avgStar;
	}
	
	// 나의 추천수 총합 구하는 서비스구문
		public int getMyDdabong(Member momoMember) {
			List<Review> myR = this.reviewRepository.findByAuthor(momoMember);
			int totalDdabong = 0;
			for(int i=0; i<myR.size(); i++) {
				totalDdabong += myR.get(i).getDdabong().size();
		}
			return totalDdabong;
		}
}

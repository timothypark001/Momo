package com.momo.board.faq.category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.member.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqCategoryService {

	private final FaqCategoryRepository faqCategoryRepository;
	
	//카테고리 생성
		public void createFaqCategory(String category, Member author) {
			FaqCategory faqCategory = new FaqCategory();
			faqCategory.setCategory(category);
			faqCategory.setCreateDate(LocalDateTime.now());
			faqCategory.setAuthor(author);
			this.faqCategoryRepository.save(faqCategory);
		}
		
		//카테고리 가져오기
		public FaqCategory getFaqCategory(Integer no) {
			Optional<FaqCategory> faqCategory = this.faqCategoryRepository.findById(no);
			if(faqCategory.isPresent()) {
				return faqCategory.get();
			}else {
				throw new DataNotFoundException("데이터가 없습니다");
			}
		}
		
		public List<FaqCategory> getFaqCategoryList(){
			return this.faqCategoryRepository.findAll();
		}
		
		public FaqCategory getFaqCategoryList1(String Category){
			return this.faqCategoryRepository.findByCategory(Category);
		}
		
		public void delete(Integer no) {
			this.faqCategoryRepository.deleteById(no);
		}
		
		public void update(Integer no, String category) {
			Optional<FaqCategory> faqCategory =this.faqCategoryRepository.findById(no);
			FaqCategory c =faqCategory.get();
			c.setCategory(category);
			c.setUpdateDate(LocalDateTime.now());
			this.faqCategoryRepository.save(c);
		}
	
	
	
}

package com.momo.board.faq.posting;

import java.time.LocalDateTime;
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
import com.momo.board.faq.category.FaqCategory;
import com.momo.member.Member;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqPostingService {

	private final FaqPostingRepository faqPostingRepository;

	public void createFaq( String subject, String content, Member author) {
		FaqPosting faq = new FaqPosting();
		faq.setSubject(subject);
		faq.setContent(content);
		faq.setCreateDate(LocalDateTime.now());
		faq.setAuthor(author);
		this.faqPostingRepository.save(faq);
	}
	
	public Page<FaqPosting> getList(int page, String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<FaqPosting> spec = search(kw);
		return this.faqPostingRepository.findAll(spec,pageable);
	}
	
	public FaqPosting getfaqPosting(Integer no) {
		Optional<FaqPosting> faq = this.faqPostingRepository.findById(no);
		if(faq.isPresent()) {
			return faq.get();
		}else {
			throw new DataNotFoundException("데이터가 없습니다");
		}	
	}
	
	public void delete(Integer no) {
		this.faqPostingRepository.deleteById(no);
	}
	

	public void update(Integer no, String subject, String content) {
		Optional<FaqPosting> posting=this.faqPostingRepository.findById(no);
		FaqPosting p = posting.get();
		p.setSubject(subject);
		p.setContent(content);
		p.setUpdateDate(LocalDateTime.now());
		this.faqPostingRepository.save(p);
	}
	
	
	 private Specification<FaqPosting> search(String kw) {
	        return new Specification   <>() {
	            private static final long serialVersionUID = 1L;
	            @Override
	            public Predicate toPredicate(Root<FaqPosting> faq, CriteriaQuery<?> query, CriteriaBuilder cb) {
	                query.distinct(true);   
	                return cb.or(cb.like(faq.get("subject"), "%" + kw + "%"), 
	                        cb.like(faq.get("content"), "%" + kw + "%"));            
	            }
	        };
	    }
	
	
		
	 
		public List<FaqPosting> getList1(FaqCategory faqCategory){
			return this.faqPostingRepository.findByFaqCategory(faqCategory);
		}
		
	
}

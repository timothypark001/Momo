package com.momo.board.faq.category;

import org.springframework.data.jpa.repository.JpaRepository;



public interface FaqCategoryRepository extends JpaRepository<FaqCategory, Integer>{

	
	FaqCategory findByCategory(String category);
	

	
	
}

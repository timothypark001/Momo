package com.momo.board.notice.posting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticePostingRepository extends JpaRepository<NoticePosting, Integer>{

	//사용자지정 쿼리문
	Page<NoticePosting> findAll(Pageable pageable);
	
	//검색
	Page<NoticePosting> findAll(Specification<NoticePosting> spec, Pageable pageable);
	
}

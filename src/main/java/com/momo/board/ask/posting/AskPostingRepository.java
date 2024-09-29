package com.momo.board.ask.posting;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.momo.member.Member;

public interface AskPostingRepository extends JpaRepository<AskPosting, Integer>{
	
	Page<AskPosting> findAll(Specification<AskPosting> spec , Pageable pageable);
	
	@Query("select a from AskPosting a where a.subject like %:subject% and a.author=:author")
	Page<AskPosting> findByAuthorAndSubject(@Param(value="author")Member author, @Param(value="subject")String subject, Pageable pageable);
	
	List<AskPosting> findByAuthor(Member member);
}

package com.momo.board.free.posting;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.momo.board.ask.posting.AskPosting;
import com.momo.member.Member;

public interface FreePostingRepository extends JpaRepository<FreePosting, Integer> {

	Page<FreePosting> findAll(Pageable pageable);
	
	Page<FreePosting> findAll(Specification<FreePosting> spec , Pageable pageable);
	
	@Query("select f from FreePosting f where f.subject like %:subject% and f.author=:author")
	Page<FreePosting> findByAuthorAndSubject(@Param(value="author")Member author, @Param(value="subject")String subject, Pageable pageable);

	List<FreePosting> findByAuthor(Member member);
}

package com.momo.board.ask.comment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.momo.board.ask.posting.AskPosting;
import com.momo.member.Member;

public interface AskCommentRepository extends JpaRepository<AskComment, Integer>{

	Page<AskComment> findAllByAskPosting(AskPosting askPosting , Pageable pageable);
	
	@Query("select a from AskComment a where a.author=:author and a.content like %:content%")
	Page<AskComment> findByAuthorAndContent(@Param(value="author")Member author, @Param(value="content")String content, Pageable pageable);
	
	List<AskComment> findByAuthor(Member member);
}

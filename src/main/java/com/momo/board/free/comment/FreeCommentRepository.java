package com.momo.board.free.comment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.momo.member.Member;

public interface FreeCommentRepository extends JpaRepository<FreeComment, Integer> {
	
	@Query("select f from FreeComment f where f.author=:author and f.content like %:content%")
	Page<FreeComment> findByAuthorAndContent(@Param(value="author")Member author, @Param(value="content")String content, Pageable pageable);
	
	List<FreeComment> findByAuthor(Member member);
}

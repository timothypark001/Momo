package com.momo.board.free.comment.re;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.member.Member;

public interface FreeCommentReplyRepository extends JpaRepository<FreeCommentReply, Integer>{

	List<FreeCommentReply> findByAuthor(Member member);
	
}

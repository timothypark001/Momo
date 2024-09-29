package com.momo.board.inquiry.posting;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.board.inquiry.comment.InquiryComment;
import com.momo.member.Member;


public interface InquiryPostingRepository extends JpaRepository<InquiryPosting, Integer> {
	
	Page<InquiryPosting> findAll (Pageable pageable);
	
	@Query("select i from InquiryPosting i where i.subject like %:subject% order by i.createDate desc")
	List<InquiryPosting> findAllOrderByDesc(@Param(value="subject")String subject);
		          

    Page<InquiryPosting> findByCommentList(List<InquiryComment> commentList,Pageable pageable);
    
    
    //메소드를 이용한 검색
    Page<InquiryPosting> findAll(Specification<InquiryPosting> spec, Pageable pageable);
     
    
    //직접 쿼리문 작성하여 검색
    @Query("select i from InquiryPosting i where i.subject like %:subject% and i.author=:author")
    Page<InquiryPosting> findByAuthorAndSubject(@Param(value="author")Member author, @Param(value="subject")String subject, Pageable pageable);
    


}

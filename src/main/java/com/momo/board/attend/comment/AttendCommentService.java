package com.momo.board.attend.comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.momo.member.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendCommentService 
{
	private final AttendCommentRepository attendCommentRepository;
	
	public void create(String content, String membernick, String memberid, Member member)
	{
		AttendComment attendComment = new AttendComment();
		
		attendComment.setContent(content);
		attendComment.setMemberid(memberid);
		attendComment.setMembernick(membernick);
		attendComment.setLocalDate(LocalDate.now());
		attendComment.setCreateDate(LocalDateTime.now());
		attendComment.setTodaysCommentAvilivable(1);
		attendComment.setAuthor(member);
		this.attendCommentRepository.save(attendComment);
	}
	
	public AttendComment getAttend(String memberid, LocalDate today)
	{
		Optional<AttendComment> attend = this.attendCommentRepository.findByMemberidAndLocalDate(memberid, today);
		if(attend.isPresent()) {
			return attend.get();
		} else if(attend.isEmpty()) {
			return null;
		}
		return null;
		
	}
	
	public void delete(Integer id)
	{
		this.attendCommentRepository.deleteById(id);
	}
	
	public Page<AttendComment> getList(int page, LocalDate today)
	{	
		
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.attendCommentRepository.findByLocalDate(pageable, today);
	}
}

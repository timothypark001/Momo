package com.momo.board.attend.comment;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendCommentRepository extends JpaRepository<AttendComment, Integer>
{
	Page<AttendComment> findByLocalDate (Pageable pageable, LocalDate today);
	Optional<AttendComment> findByMemberidAndLocalDate(String memberid, LocalDate today);
}

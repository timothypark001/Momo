package com.momo.image;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.member.Member;

public interface ImageRepository extends JpaRepository<Image, Integer> {

	Optional<Image> findByAuthor(Member member);
}

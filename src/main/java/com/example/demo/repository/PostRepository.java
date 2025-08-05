package com.example.demo.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
	// createdAt（作成日時）で降順に並べる
	List<Post> findAllByOrderByCreatedAtDesc();
}

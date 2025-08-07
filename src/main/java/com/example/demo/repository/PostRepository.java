package com.example.demo.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Post;
import com.example.demo.entity.User;

public interface PostRepository extends JpaRepository<Post, Integer> {
	// createdAt（作成日時）で降順に並べる
	List<Post> findAllByOrderByCreatedAtDesc();
	// 指定されたユーザーの投稿を取得（作成日時で降順）
	List<Post> findByUserInOrderByCreatedAtDesc(List<User> users);
	//
	List<Post> findByUser_IdOrderByCreatedAtDesc(Integer userId);

	List<Post> findByUser_IdInOrderByCreatedAtDesc(List<Integer> ids);

}

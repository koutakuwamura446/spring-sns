package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Follow;
import com.example.demo.entity.User;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

	// すでにフォローしているか
	boolean existsByFollowingAndFollowed(User following, User followed);

	// 自分がフォローしているユーザー一覧
	List<Follow> findByFollowing(User following);

	// 自分をフォローしているユーザー一覧
	List<Follow> findByFollowed(User followed);

	// フォロー解除
	void deleteByFollowingAndFollowed(User following, User followed);

	// 指定されたユーザーのフォロー関係を取得（1対1）
	Follow findByFollowingAndFollowed(User following, User followed);

	// following_id と followed_id の組み合わせが存在するかチェックするメソッド
	boolean existsByFollowingIdAndFollowedId(Integer followingId, Integer followedId);
	
	List<Follow> findByFollowingId(Integer followingId);
	
	// 自分がフォローしているユーザー数（フォロー数）
	Integer countByFollowingId(Integer userId);

    // 自分をフォローしているユーザー数（フォロワー数）
	 Integer countByFollowedId(Integer userId);
}
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
}
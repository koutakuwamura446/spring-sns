package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //主キーの値を自動で設定 するために使われるアノテーション
	//フィールド
	//投稿ID
	private Integer id;
	// だれの投稿か
	@Column(name = "user_id")
	private String userId;
	// 投稿内容
	private String post;
	// 登録日
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	// 更新日
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// コンストラクタ
	public Post() {
	}

	public Post(String userId, String post, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.userId = userId;
		this.post = post;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	//ゲッターとセッター
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	
}

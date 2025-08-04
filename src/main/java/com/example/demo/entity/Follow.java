package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "follows")
public class Follow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //主キーの値を自動で設定 するために使われるアノテーション
	//フィールド
	//フォローID
	private Integer id;
	// フォローした人のID番号
	@Column(name = "following_id")
	private String followingId;
	// フォローされた人のID番号
	@Column(name = "followed_id")
	private String followedId;
	// 登録日
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	// 更新日
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	//コンストラクタ
	public Follow() {
	}

	public Follow(String followingId, String followedId, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.followingId = followingId;
		this.followedId = followedId;
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

	public String getFollowingId() {
		return followingId;
	}

	public void setFollowingId(String followingId) {
		this.followingId = followingId;
	}

	public String getFollowedId() {
		return followedId;
	}

	public void setFollowedId(String followedId) {
		this.followedId = followedId;
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

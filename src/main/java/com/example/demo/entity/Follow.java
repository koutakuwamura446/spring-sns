package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "follows")
public class Follow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //主キーの値を自動で設定 するために使われるアノテーション
	//フィールド
	//フォローID
	private Integer id;

	// フォローしたユーザー（自分側のユーザー）
	// Followテーブルの「following_id」列に対応し、Userエンティティと多対一（ManyToOne）の関係を構築
	@ManyToOne
	@JoinColumn(name = "following_id", nullable = false)
	private User following;

	// フォローされたユーザー（相手側のユーザー）
	// Followテーブルの「followed_id」列に対応し、Userエンティティと多対一（ManyToOne）の関係を構築
	@ManyToOne
	@JoinColumn(name = "followed_id", nullable = false)
	private User followed;

	// 登録日
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	// 更新日
	@Column(name = "updated_at", nullable = false, updatable = false)
	private LocalDateTime updatedAt;

	//コンストラクタ
	public Follow() {
	}

	public Follow(User following, User followed) {
		this.following = following;
		this.followed = followed;
		this.createdAt = LocalDateTime.now(); // 現在時刻で設定
		this.updatedAt = LocalDateTime.now(); // 現在時刻で設定
	}

	public Follow(User following, User followed, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.following = following;
		this.followed = followed;
		this.createdAt = LocalDateTime.now(); // 現在時刻で設定
		this.updatedAt = LocalDateTime.now(); // 現在時刻で設定
	}

	//ゲッターとセッター
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getFollowing() {
		return following;
	}

	public void setFollowing(User following) {
		this.following = following;
	}

	public User getFollowed() {
		return followed;
	}

	public void setFollowed(User followed) {
		this.followed = followed;
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

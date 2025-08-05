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
@Table(name = "posts")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //主キーの値を自動で設定 するために使われるアノテーション
	//フィールド
	//投稿ID
	private Integer id;
	 // Userエンティティとの多対一の関係を定義
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
	// 投稿内容
    @Column(name = "post")  // DBのカラム名は post に対応
    private String postText;

	// 登録日
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	// 更新日
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// コンストラクタ
	public Post() {
	}

	public Post(User user, String postText) {
		this.user = user;
		this.postText = postText;
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

	public User getUser() {
		return user;
	}

	public void setUserId(User user) {
		this.user = user;
	}

	public String getPost() {
		return postText;
	}

	public void setPost(String postText) {
		this.postText = postText;
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

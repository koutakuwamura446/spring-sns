package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //主キーの値を自動で設定 するために使われるアノテーション
	//フィールド
	// ユーザーID
	private Integer id;
	// ユーザー名
	private String username;
	// メールアドレス
	private String mail;
	// ログインパス
	private String password;
	// 自己紹介文
	private String bio;
	// ユーザーアイコン
	@Column(name = "icon_path")
	private String iconPath;
	// 登録日
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	// 更新日
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// コンストラクタ
	public User() {
	}

	public User(String username, String mail, String password, String bio, String iconPath, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.username = username;
		this.mail = mail;
		this.password = password;
		this.bio = bio;
		this.iconPath = iconPath;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
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
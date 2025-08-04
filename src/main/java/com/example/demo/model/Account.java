package com.example.demo.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class Account {
	//フィールド
	// メールアドレス
	private String mail;
	// ログインパス
	private String password;

	//コンストラクタ
	public Account() {

	}

	public Account(String mail, String password) {
		this.mail = mail;
		this.password = password;
	}

	//ゲッターとセッター
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

	

}
/*modelはitemsように初期データが入っているテーブルではなく
 初期が空のテーブルやテーブルがないデータの文クラス的な役割を果たしている
 */
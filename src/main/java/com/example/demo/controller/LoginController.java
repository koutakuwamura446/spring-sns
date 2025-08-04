package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Controller
public class LoginController {
	@Autowired
	HttpSession session;
	@Autowired
	UserRepository userRepository;
	

	// ログイン画面の表示
	@GetMapping({ "/", "/login", "/logout" })
	public String index(@RequestParam(name = "error", defaultValue = "") String error,
			Model model) {
		// セッション情報を全てクリアする
		session.invalidate();
		//エラーパラメータのチェック
		if (error.equals("notLoggedIn")) {
			model.addAttribute("message", "ログインしてください");
		}

		return "login";
	}

	//ログイン処理
	@PostMapping("/login")
	public String login(
			@RequestParam("mail") String mail,
			@RequestParam("password") String password,
			Model model,
			HttpSession session) {
		// 入力チェック：メールアドレスまたはパスワードが未入力の場合はエラーを返す
		if (mail.isEmpty() || password.isEmpty()) {
			model.addAttribute("message", "メールアドレスとパスワードを入力してください");
			return "login";
		}

		// ユーザー認証：メールアドレスとパスワードに一致するユーザーを検索
		List<User> userList = userRepository.findByMailAndPassword(mail, password);
		// 検索結果が空（該当ユーザーが存在しない）場合はエラーメッセージを表示
		if (userList.isEmpty()) {
			model.addAttribute("message", "アカウントがみつかりません");
			return "login";
		}

		// ログインに成功したユーザー情報を取得
		User user = userList.get(0);

		session.setAttribute("user", user);

		// ログイン後は商品一覧ページなどホーム画面にリダイレクト
		return "redirect:/top";
	}

}

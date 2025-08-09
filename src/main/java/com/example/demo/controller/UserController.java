package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class UserController {
	@Autowired
	HttpSession session;

	@Autowired
	FollowRepository followRepository;
	@Autowired
	PostRepository postRepository;
	@Autowired
	UserRepository userRepository;

	// フォロー画面の表示
	@GetMapping("/profile")
	public String following(@RequestParam(name = "error", defaultValue = "") String error,
			Model model, HttpSession session) {

		// ログインユーザーをセッションから取得
		User user = (User) session.getAttribute("user");
		

		// 未ログインユーザーが直接マイページにアクセスした場合にログインページへ
		if (user == null) {
			return "redirect:/login";
		}
		model.addAttribute("user", user);

		return "update";
	}
}

package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.PostRepository;

@Controller
public class PostController {
	@Autowired
	HttpSession session;
	@Autowired
	PostRepository postRepository;

	// トップ画面の表示
	@GetMapping("/top")
	public String top(@RequestParam(name = "error", defaultValue = "") String error,
			Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);

		List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
		model.addAttribute("postList", postList);

		return "top";
	}

	// トップ画面の表示
	@PostMapping("/top")
	public String post(@RequestParam(name = "post", defaultValue = "") String postText,
			Model model, HttpSession session) {
		//セッションでユーザー情報取得
		User user = (User) session.getAttribute("user");
		//ユーザーの情報を記載できるようにする
		model.addAttribute("user", user);
		
		// 入力チェックに使うフラグ
		boolean hasError = false;
		// 投稿のチェック
		if (postText.length() < 1 || postText.length() > 150) {
			model.addAttribute("postTextMessage", "※投稿文は1文字以上,150文字以内で入力してください");
			hasError = true;
		}
		// 何か1つでもエラーがあれば、再度「トップ画面」を表示
		if (hasError) {
			// エラー時も投稿一覧を表示
			List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
			model.addAttribute("postList", postList);
			return "top";
		}

		//Postオブジェクトの生成
		Post post = new Post(user, postText);
		//postテーブルへの反映
		postRepository.save(post);


		return "redirect:/top";
	}
}

package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	//投稿機能
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
		// エラーがあれば、再度「トップ画面」を表示
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

	//編集機能
	@PostMapping("/posts/edit/{id}")
	public String update(
			@PathVariable("id") Integer id,
			@RequestParam(name = "post", defaultValue = "") String postText,
			Model model, HttpSession session) {
		//セッションでユーザー情報取得
		User user = (User) session.getAttribute("user");
		//ユーザーの情報を記載できるようにする
		model.addAttribute("user", user);

		// 編集対象の投稿をDBから取得
		Post post = postRepository.findById(id).orElse(null);

		// 存在しない場合はトップにリダイレクト
		if (post == null) {
			return "redirect:/top";
		}
		// 投稿のチェック
		if (postText.length() < 1 || postText.length() > 150) {
			return "redirect:/top";
		}
		// セッションのユーザーと投稿者が一致しているか確認（不正操作防止）
		if (!post.getUser().getId().equals(user.getId())) {
			return "redirect:/top";
		}

		// 内容を更新
		post.setPost(postText);
		post.setUpdatedAt(LocalDateTime.now());

		// 保存（上書き）
		postRepository.save(post);

		return "redirect:/top";
	}

	// 削除処理
	@PostMapping("/posts/delete/{id}")
	public String delete(@PathVariable("id") Integer id, Model model) {
		//セッションでユーザー情報取得
		User user = (User) session.getAttribute("user");
		//ユーザーの情報を記載できるようにする
		model.addAttribute("user", user);
		// 編集対象の投稿をDBから取得
		Post post = postRepository.findById(id).orElse(null);

		// 存在しない場合はトップにリダイレクト
		if (post == null) {
			return "redirect:/top";
		}
		// セッションのユーザーと投稿者が一致しているか確認（不正操作防止）
		if (!post.getUser().getId().equals(user.getId())) {
			return "redirect:/top";
		}
		// itemsテーブルから削除（DELETE）
		postRepository.deleteById(id);

		return "redirect:/top";
	}
}

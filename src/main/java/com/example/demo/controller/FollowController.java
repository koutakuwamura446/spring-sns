package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Follow;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.PostRepository;

@Controller
public class FollowController {

	@Autowired
	HttpSession session;

	@Autowired
	FollowRepository followRepository;
	@Autowired
	PostRepository postRepository;

	// フォロー画面の表示
	@GetMapping("/following")
	public String following(@RequestParam(name = "error", defaultValue = "") String error,
			Model model, HttpSession session) {

		// ログインユーザーをセッションから取得
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);

		if (user != null) {
			// 自分がフォローしているユーザー一覧を取得
			List<Follow> followingList = followRepository.findByFollowing(user);

			// Followエンティティからfollowed（フォローされた側）だけ抽出
			List<User> followedUsers = followingList.stream()
					.map(Follow::getFollowed)
					.toList();

			// フォローしているユーザーの投稿だけ取得
			List<Post> postList = postRepository.findByUserInOrderByCreatedAtDesc(followedUsers);

			model.addAttribute("postList", postList);
			model.addAttribute("followingList", followingList); // 必要なら
		}

		return "following";
	}
	// フォロー画面の表示
		@GetMapping("/followed")
		public String followed(@RequestParam(name = "error", defaultValue = "") String error,
				Model model, HttpSession session) {

			// ログインユーザーをセッションから取得
			User user = (User) session.getAttribute("user");
			model.addAttribute("user", user);

			if (user != null) {
				// 自分がフォローされているユーザー一覧を取得
				List<Follow> followedList = followRepository.findByFollowed(user);

				// Followエンティティからfollowed（フォローている側）だけ抽出
				List<User> followingUsers = followedList.stream()
						.map(Follow::getFollowing)
						.toList();

				// フォローされているユーザーの投稿だけ取得
				List<Post> postList = postRepository.findByUserInOrderByCreatedAtDesc(followingUsers);

				model.addAttribute("postList", postList);
				model.addAttribute("followedList", followedList); // 必要なら
			}

			return "followed";
		}
}

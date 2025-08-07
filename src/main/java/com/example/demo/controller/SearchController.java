package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class SearchController {
	@Autowired
	HttpSession session;
	@Autowired
	UserRepository userRepository;
	@Autowired
	FollowRepository followRepository;

	// 検索画面の表示
	@GetMapping("/search")
	public String index(
			@RequestParam(name = "error", defaultValue = "") String error,
			Model model, HttpSession session) {
		// セッションからログインユーザー情報を取得
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		// すべてのユーザー一覧を取得（ログインユーザー以外などに絞ってもよい）
		List<User> userList = userRepository.findAll();
		// ログインユーザー本人を除外
		userList.removeIf(u -> u.getId().equals(user.getId()));

		model.addAttribute("userList", userList);
		// ▼ フォロー状態のマップを作成
		// userList にいる各ユーザーについて、ログインユーザーがそのユーザーをフォローしているかを判定
		// ユーザーIDをキー、フォロー状態（true/false）を値としてMapに保存する
		Map<Integer, Boolean> isFollowingMap = new HashMap<>();
		for (User u : userList) {
			boolean isFollowing = followRepository.existsByFollowingIdAndFollowedId(user.getId(), u.getId());
			isFollowingMap.put(u.getId(), isFollowing);
		}
		// 作成したフォロー状態のMapをモデルに追加し、ビューでユーザーごとのフォロー状態を参照可能にする
		model.addAttribute("isFollowingMap", isFollowingMap);

		return "search";
	}

	// 検索処理
	@PostMapping("/search")
	public String search(
			@RequestParam(name = "key", defaultValue = "") String key,
			Model model, HttpSession session) {

		// セッションからログインユーザー情報を取得
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);

		// 名前であいまい検索（部分一致）
		List<User> userList = userRepository.findByUsernameContaining(key);
		// ログインユーザー本人を除外
		userList.removeIf(u -> u.getId().equals(user.getId()));
		// モデルにデータを渡す
		model.addAttribute("userList", userList);
		model.addAttribute("key", key);

		// フォロー状態のマップを作成
		Map<Integer, Boolean> isFollowingMap = new HashMap<>();
		for (User u : userList) {
			boolean isFollowing = followRepository.existsByFollowingIdAndFollowedId(user.getId(), u.getId());
			isFollowingMap.put(u.getId(), isFollowing);
		}
		model.addAttribute("isFollowingMap", isFollowingMap);

		return "search";
	}

}

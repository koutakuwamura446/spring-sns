package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Follow;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class FollowController {

	@Autowired
	HttpSession session;

	@Autowired
	FollowRepository followRepository;
	@Autowired
	PostRepository postRepository;
	@Autowired
	UserRepository userRepository;

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

	// フォロワー画面の表示
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

	// フォロー機能
	@PostMapping("/follow/{followedId}")
	public String follow(@PathVariable("followedId") Integer followedId,
			HttpSession session,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		// セッションからログイン中のユーザー（フォローする側）を取得
		User following = (User) session.getAttribute("user");
		if (following == null) {
			// 未ログインの場合はログイン画面へリダイレクト
			return "redirect:/login";
		}

		// フォローされる側のユーザーをIDだけ設定した状態で作成（DBからの取得はしていない）
		User followed = new User();
		followed.setId(followedId);

		// すでにフォローしているか確認（重複フォロー防止）
		if (followRepository.findByFollowingAndFollowed(following, followed) == null) {
			// フォロー情報を新規作成
			Follow follow = new Follow();
			follow.setFollowing(following);
			follow.setFollowed(followed);

			// 登録日時と更新日時を現在時刻で設定
			LocalDateTime now = LocalDateTime.now();
			follow.setCreatedAt(now);
			follow.setUpdatedAt(now);

			// フォロー情報をDBに保存
			followRepository.save(follow);
		}

		 // 押した元の画面にリダイレクト（Refererを利用）
	    String referer = request.getHeader("Referer");
	    return "redirect:" + (referer != null ? referer : "/");
	}

	// アンフォロー機能
	@PostMapping("/unfollow/{followedId}")
	public String unfollow(@PathVariable("followedId") Integer followedId,
			HttpSession session,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		// セッションからログイン中のユーザー（アンフォローする側）を取得
		User following = (User) session.getAttribute("user");
		if (following == null) {
			// 未ログインの場合はログイン画面へリダイレクト
			return "redirect:/login";
		}

		// アンフォロー対象のユーザーをIDだけ設定した状態で作成
		User followed = new User();
		followed.setId(followedId);

		// フォロー情報が存在するか確認
		Follow existingFollow = followRepository.findByFollowingAndFollowed(following, followed);
		if (existingFollow != null) {
			// 存在すればDBから削除（アンフォロー処理）
			followRepository.delete(existingFollow);
		}

		// 押した元の画面にリダイレクト（Refererを利用）
	    String referer = request.getHeader("Referer");
	    return "redirect:" + (referer != null ? referer : "/");
	}

	// ユーザープロフィール表示
	@GetMapping("/profile/{id}")
	public String showUserProfile(@PathVariable("id") Integer id, Model model, HttpSession session) {

	    // DBから対象ユーザー情報を取得
	    User targetUser = userRepository.findById(id).orElse(null);

	    // ユーザーが存在しない場合はエラーページへ
	    if (targetUser == null) {
	        return "following"; // ここは本当はエラーページ用のテンプレート名にした方がいいかも
	    }

	    // セッションからログインユーザーを取得
	    User loginUser = (User) session.getAttribute("user");

	    // 投稿を取得
	    List<Post> posts = postRepository.findByUser_IdOrderByCreatedAtDesc(id);

	    // ログインユーザーがこのユーザーをフォローしているかをチェック
	    boolean isFollowing = followRepository.existsByFollowingIdAndFollowedId(loginUser.getId(), id);

	    // 対象ユーザーがフォローしているユーザーID一覧を取得
	    List<Follow> followingList = followRepository.findByFollowingId(id);
	    List<Integer> followedUserIds = followingList.stream()
	            .map(f -> f.getFollowed().getId())
	            .collect(Collectors.toList());

	    // フォローしているユーザーの投稿を取得（空リストも考慮）
	    List<Post> followingPosts = followedUserIds.isEmpty()
	            ? new ArrayList<>()
	            : postRepository.findByUser_IdInOrderByCreatedAtDesc(followedUserIds);

	    // --- ここから追加 ---
	    // 投稿ごとのユーザーに対するフォロー状態を Map にまとめる
	    Map<Integer, Boolean> isFollowingMap = new HashMap<>();
	    for (Post p : posts) {
	        boolean followingFlag =
	                followRepository.existsByFollowingIdAndFollowedId(loginUser.getId(), p.getUser().getId());
	        isFollowingMap.put(p.getUser().getId(), followingFlag);
	    }
	    // --- ここまで追加 ---

	    // モデルに渡す
	    model.addAttribute("isFollowing", isFollowing);      // プロフィール対象ユーザーへのフォロー状態
	    model.addAttribute("isFollowingMap", isFollowingMap); // 投稿ユーザーごとのフォロー状態
	    model.addAttribute("user", loginUser);
	    model.addAttribute("targetUser", targetUser);
	    model.addAttribute("posts", posts);
	    model.addAttribute("followingPosts", followingPosts);

	    return "otherProfile";
	}


}

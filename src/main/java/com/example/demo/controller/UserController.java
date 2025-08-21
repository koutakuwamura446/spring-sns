package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

	// profile画面の表示
	@GetMapping("/profile")
	public String profile(@RequestParam(name = "error", defaultValue = "") String error,
			Model model, HttpSession session) {

		//セッションでユーザー情報取得
		User user = (User) session.getAttribute("user");
		//ユーザーの情報を記載できるようにする
		model.addAttribute("user", user);

		// 未ログインユーザーが直接マイページにアクセスした場合にログインページへ
		if (user == null) {
			return "redirect:/login";
		}

		return "update";
	}

	//更新処理
	@PostMapping("/profile/update")
	public String update(
			@RequestParam("username") String username,
			@RequestParam("mail") String mail,
			@RequestParam("password") String password,
			@RequestParam("password_confirm") String password_confirm,
			@RequestParam(value = "bio", required = false) String bio,
			@RequestParam("iconPath") MultipartFile iconFile,
			Model model, HttpSession session) throws IOException {

		//セッションでユーザー情報取得
		User user = (User) session.getAttribute("user");
		//ユーザーの情報を記載できるようにする
		model.addAttribute("user", user);

		// 入力チェック用フラグ
		boolean hasError = false;

		// ===== 名前チェック =====
		if (username.isEmpty()) {
			model.addAttribute("usernameMessage", "※名前を入力してください");
			hasError = true;
		} else if (username.length() < 2 || username.length() > 12) {
			model.addAttribute("usernameMessage", "※名前は2文字以上,12文字以内で入力してください");
			hasError = true;
		}

		// ===== メールチェック =====
		if (mail.isEmpty()) {
			model.addAttribute("mailMessage", "※メールアドレスを入力してください");
			hasError = true;
		} else if (mail.length() < 5 || mail.length() > 40) {
			model.addAttribute("mailMessage", "※メールアドレスは5文字以上,40文字以内で入力してください");
			hasError = true;
		} else if (!mail.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
			model.addAttribute("mailMessage", "※正しい形式のメールアドレスを入力してください（例: example@example.com）");
			hasError = true;
		}

		// ===== パスワードチェック =====
		if (password.isEmpty()) {
			model.addAttribute("passwordMessage", "※パスワードを入力してください");
			hasError = true;
		} else if (password.length() < 8 || password.length() > 20) {
			model.addAttribute("passwordMessage", "※パスワードは8文字以上,20文字以内で入力してください");
			hasError = true;
		} else if (!password.matches("^[a-zA-Z0-9]+$")) {
			model.addAttribute("passwordMessage", "※パスワードは英数字のみで入力してください");
			hasError = true;
		}

		// ===== 確認用パスワード =====
		if (password_confirm.isEmpty()) {
			model.addAttribute("password_confirmMessage", "※確認用パスワードを入力してください");
			hasError = true;
		} else if (!password.equals(password_confirm)) {
			model.addAttribute("message", "※入力されたパスワードと一致しません");
			hasError = true;
		}

		// ===== 自己紹介 =====
		if (bio.isEmpty()) {
			model.addAttribute("bioMessage", "※自己紹介を入力してください");
			hasError = true;
		} else if (bio.length() > 151) {
			model.addAttribute("bioMessage", "※自己紹介は150文字以内で入力してください");
			hasError = true;
		}

		// ===== 自己紹介 =====
		if (bio.isEmpty()) {
			model.addAttribute("bioMessage", "※自己紹介を入力してください");
			hasError = true;
		} else if (bio.length() > 151) {
			model.addAttribute("bioMessage", "※自己紹介は150文字以内で入力してください");
			hasError = true;
		}

		// ===== アイコン画像チェック =====
		if (!iconFile.isEmpty()) {
			// 元ファイル名
			String originalFileName = iconFile.getOriginalFilename();

			// 拡張子を小文字で取得
			String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

			// 許可する拡張子
			List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "bmp", "gif", "svg");

			if (!allowedExtensions.contains(extension)) {
				model.addAttribute("iconMessage", "※画像ファイル（jpg, jpeg, png, bmp, gif, svg）のみアップロード可能です");
				hasError = true;
			}
		}

		// ===== エラーがあれば更新画面に戻る =====
		if (hasError) {
			model.addAttribute("user", user);
			return "update";
		}

		// ファイルアップロード処理（ファイル名だけDBに保存）
		if (!iconFile.isEmpty()) {
			// 元ファイル名
			String originalFileName = iconFile.getOriginalFilename();

			// 拡張子を取得（例: .png）
			String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

			// 新しいファイル名を作成（例：UUID + 拡張子）
			String fileName = UUID.randomUUID().toString() + extension;

			// 保存先ディレクトリ（開発用）
			String uploadDir = "src/main/resources/static/images/icon-images";

			// フォルダがなければ作成
			Files.createDirectories(Paths.get(uploadDir));

			// ファイル保存パス
			Path savePath = Paths.get(uploadDir, fileName);

			// ファイル保存（上書き許可）
			Files.copy(iconFile.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

			// DBにはファイル名だけ保存（パスは入れない）
			user.setIconPath(fileName);
		}

		// ===== 他の項目を更新 =====
		user.setUsername(username);
		user.setMail(mail);
		user.setPassword(password);
		user.setBio(bio);

		// 更新処理の最後
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);
		session.setAttribute("user", user);

		return "redirect:/top";
	}

}

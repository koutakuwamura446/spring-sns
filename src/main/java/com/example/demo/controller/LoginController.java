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
		// 入力チェックに使うフラグ
		boolean hasError = false;

		// 入力チェック：メールアドレスまたはパスワードが未入力の場合はエラーを返す
		if (mail.isEmpty()) {
			model.addAttribute("mailMessage", "メールアドレスを入力してください");
			hasError = true;
		}
		if (password.isEmpty()) {
			model.addAttribute("passwordMessage", "パスワードを入力してください");
			hasError = true;
		}
		// 何か1つでもエラーがあれば、再度「新規登録画面(createAccount)」を表示
		if (hasError) {
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

	// 新規登録画面の表示
	@GetMapping("/users/new")
	public String create(
			@RequestParam(name = "error", defaultValue = "") String error,
			Model model) {
		// セッション情報を全てクリアする
		session.invalidate();
		// エラーパラメータのチェック
		if (error.equals("notLoggedIn")) {
			model.addAttribute("message", "全ての項目を入力してください");
		}
		return "createAccount";
	}

	//新規登録の実行処理
	@PostMapping("/users/add")
	public String add(
			@RequestParam("username") String username,
			@RequestParam("mail") String mail,
			@RequestParam("password") String password,
			@RequestParam("password_confirm") String password_confirm,
			Model model) {

		// 入力チェックに使うフラグ
		boolean hasError = false;

		// 名前のチェック
		if (username.isEmpty()) {
			model.addAttribute("usernameMessage", "※名前を入力してください");
			hasError = true;
		} else if (username.length() < 2 || username.length() > 12) {
			model.addAttribute("usernameMessage", "※名前は2文字以上,12文字以内で入力してください");
			hasError = true;
		}
		//メールのチェック
		if (mail.isEmpty()) {
			model.addAttribute("mailMessage", "※メールアドレスを入力してください");
			hasError = true;
		} else if (mail.length() < 5 || mail.length() > 40) {
			model.addAttribute("mailMessage", "※メールアドレスは5文字以上,40文字以内で入力してください");
			hasError = true;
		} else if (!mail.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
			model.addAttribute("mailMessage", "※正しい形式のメールアドレスを入力してください（例: example@example.com）");
			hasError = true;
		} else {
			User existingUser = userRepository.findByMail(mail);
			if (existingUser != null) {
				model.addAttribute("mailMessage", "※このメールアドレスはすでに使用されています");
				hasError = true;
			}
		}

		//パスワードのチェック
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

		//確認用パスワードのチェック
		if (password_confirm.isEmpty()) {
			model.addAttribute("password_confirmMessage", "※確認用パスワードを入力してください");
			hasError = true;
		}

		// パスワードと確認用パスワードが一致していない場合
		if (!password.isEmpty() && !password_confirm.isEmpty() && !password.equals(password_confirm)) {
			model.addAttribute("message", "※入力されたパスワードと一致しません");
			hasError = true;
		}

		// 何か1つでもエラーがあれば、再度「新規登録画面(createAccount)」を表示
		if (hasError) {
			return "createAccount";
		}

		// ユーザー作成＆保存
		User user = new User(username, mail, password);
		userRepository.save(user);

		// セッションにユーザー情報セット
		session.setAttribute("user", user);

		// 登録完了画面へリダイレクト
		return "redirect:/users/register";

	}

	// 新規登録完了画面の表示
	@GetMapping("/users/register")
	public String register(Model model, HttpSession session) {
		// セッションからログインユーザー情報を取得
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		return "registerAccount";
	}
}

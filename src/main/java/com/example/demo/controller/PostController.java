package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	public String top(Model model, HttpSession session) {
	    List<Post> postList = postRepository.findAll(); // JOINされる
	    model.addAttribute("postList", postList);

	    User user = (User) session.getAttribute("user");
	    model.addAttribute("user", user);

	    return "top";
	}
}

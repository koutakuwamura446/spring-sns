package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.entity.User;
import com.example.demo.repository.FollowRepository;

@ControllerAdvice
public class CommonController {

    @Autowired
    private FollowRepository followRepository;

    @ModelAttribute
    public void addFollowCounts(
    		Model model, HttpSession session) {
    	User user = (User) session.getAttribute("user");
        if (user != null) {
            Integer userId = user.getId();
            Integer followCount = followRepository.countByFollowingId(userId);
            Integer followerCount = followRepository.countByFollowedId(userId);
            model.addAttribute("followCount", followCount);
            model.addAttribute("followerCount", followerCount);
        }
    }
}

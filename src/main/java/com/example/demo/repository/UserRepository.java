package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
List<User> findByMailAndPassword(String mail, String password);
User findByMail(String mail);
List<User> findAll();
List<User> findByUsername(String username);
//名前であいまい検索
List<User> findByUsernameContaining(String username);
}

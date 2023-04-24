package com.ssafy.a802.jaljara.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.a802.jaljara.db.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

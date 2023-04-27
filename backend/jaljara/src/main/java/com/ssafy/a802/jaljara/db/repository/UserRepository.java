package com.ssafy.a802.jaljara.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.db.entity.UserType;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySub(String sub);
    List<User> findAllByUserType(UserType userType);
}

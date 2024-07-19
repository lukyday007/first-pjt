package com.boricori.repository.userRepo;

import com.boricori.dto.request.User.UserLoginRequest;
import com.boricori.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

      User findByEmail(String email);
}

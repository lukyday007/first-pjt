package com.boricori.repository.userRepo;

import com.boricori.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT COUNT(*) + 1 " +
            "FROM User " +
            "WHERE scores > (SELECT scores FROM User WHERE email = :email)")
    int findUserRankingByEmail(@Param("email") String email);

    List<User> findAllByOrderByScoresAsc();
}

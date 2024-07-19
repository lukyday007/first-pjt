package com.boricori.repository.userRepo;

import com.boricori.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.ranking FROM (" +
            "SELECT u.email AS email, " +
            "RANK() OVER (ORDER BY u.scores DESC) AS ranking " +
            "FROM User u) ranked_users " +
            "WHERE ranked_users.email = :email")
    int findUserRankingByEmail(@Param("email") String email);
}

package com.example.demo.users.auth;

import com.example.demo.users.auth.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginRepository  extends JpaRepository<UserLogin, Long> {

    Optional<UserLogin> findByUsername(String username);

    @Query("from UserLogin ul where ul.user.id = :userId")
    Optional<UserLogin> findByUserId(@Param("userId") Long userId);

    boolean existsByUsername(String username);
}

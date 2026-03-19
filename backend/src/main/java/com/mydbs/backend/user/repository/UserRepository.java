package com.mydbs.backend.user.repository;

import com.mydbs.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndDeletedFalse(String email);

    Optional<User> findByUserCodeAndDeletedFalse(String userCode);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByUserCodeAndDeletedFalse(String userCode);

    List<User> findByDeletedFalse();

    List<User> findByFirstNameContainingIgnoreCaseAndDeletedFalseOrLastNameContainingIgnoreCaseAndDeletedFalse(
            String firstName,
            String lastName
    );
}
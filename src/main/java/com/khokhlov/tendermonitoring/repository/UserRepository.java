package com.khokhlov.tendermonitoring.repository;

import com.khokhlov.tendermonitoring.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCase(String username);

    User getUsersByIdAndUsername(long id, String username);

    Optional<User> getUserById(long id);
}

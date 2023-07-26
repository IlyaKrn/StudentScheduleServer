package com.ilyakrn.studentscheduleserver.data.repositories;

import com.ilyakrn.studentscheduleserver.data.tablemodels.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}

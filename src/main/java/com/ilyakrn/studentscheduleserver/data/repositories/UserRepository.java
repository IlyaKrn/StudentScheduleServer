package com.ilyakrn.studentscheduleserver.data.repositories;

import com.ilyakrn.studentscheduleserver.data.tablemodels.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

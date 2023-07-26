package com.ilyakrn.studentscheduleserver.services;


import com.ilyakrn.studentscheduleserver.data.repositories.UserRepository;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Role;
import com.ilyakrn.studentscheduleserver.data.tablemodels.User;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public Optional<User> getByLogin(@NonNull String login) {
        ArrayList<Role> r = new ArrayList<>();
        return userRepository.findAll().stream()
                .filter(user -> login.equals(user.getEmail()))
                .findFirst();
    }

}
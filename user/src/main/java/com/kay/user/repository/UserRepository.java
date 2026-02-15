package com.kay.user.repository;


import com.kay.user.model.Role;
import com.kay.user.model.Status;
import com.kay.user.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,String> {
    Optional<Users> findUsersByEmail(String email);


    Optional<Page<Users>> findAllByRoleOrStatus(Role role, Status status, Pageable pageable);
}

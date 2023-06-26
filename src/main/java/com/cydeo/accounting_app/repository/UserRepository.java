package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}

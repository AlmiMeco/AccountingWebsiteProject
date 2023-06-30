package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

//    Created bc Optional<User> findById() method was not working;
    User getUserById(Long id);

    List<User> findAllByCompanyId(Long companyIdOfLoggedUser);

    List<User> findAllByRoleId(long id);
}

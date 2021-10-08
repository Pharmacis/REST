package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    @Query("select distinct u from User u join fetch u.roles where u.name = :login")
    User findUserAndRolesByName(@Param("login") String name);
}

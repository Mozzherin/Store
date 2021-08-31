package org.example.repository;

import org.example.entityes.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findById(long id);
    List<User> findAll();
    User findByActivationCode(String code);
    UserDetails findByUsername(String username);
}

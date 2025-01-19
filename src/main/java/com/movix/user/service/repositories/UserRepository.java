package com.movix.user.service.repositories;

import com.movix.user.service.entities.User;
import com.movix.user.service.enums.Active;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmailId(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findAllByActive(Active active);
}

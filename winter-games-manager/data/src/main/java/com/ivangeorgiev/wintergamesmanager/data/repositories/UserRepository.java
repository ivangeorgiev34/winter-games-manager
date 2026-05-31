package com.ivangeorgiev.wintergamesmanager.data.repositories;

import com.ivangeorgiev.wintergamesmanager.data.models.Athlete;
import com.ivangeorgiev.wintergamesmanager.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByAthlete(Athlete athlete);
}

package com.ivangeorgiev.wintergamesmanager.data.repositories;

import com.ivangeorgiev.wintergamesmanager.data.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
}

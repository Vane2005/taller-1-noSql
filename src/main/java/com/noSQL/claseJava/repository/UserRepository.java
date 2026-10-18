package com.noSQL.claseJava.repository;

import com.noSQL.claseJava.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByTelephone(String telephone);
}

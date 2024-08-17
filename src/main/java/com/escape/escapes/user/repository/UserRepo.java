package com.escape.escapes.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.escape.escapes.user.model.User;

@Repository
public interface UserRepo extends MongoRepository<User, String>{
    
}

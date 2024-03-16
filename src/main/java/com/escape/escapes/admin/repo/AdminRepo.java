package com.escape.escapes.admin.repo;

import com.escape.escapes.admin.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends MongoRepository<Admin,String> {

    @Query("{'name' : ?0}")
    Optional<Admin> findByName(String name);

    @Query("{'email' : ?0}")
    Optional<Admin> findByEmail(String name);


}

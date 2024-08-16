package com.escape.escapes.token.repo;

import com.escape.escapes.token.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends MongoRepository<RefreshToken, String> {

    @Query("{'token' : ?0}")
    Optional<RefreshToken> findByToken(String token);

    @Query("{'adminInfo.email' : ?0}")
    Optional<RefreshToken> findByUserEmail(String email);
}

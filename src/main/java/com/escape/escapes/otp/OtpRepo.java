package com.escape.escapes.otp;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.Optional;
public interface OtpRepo extends MongoRepository<OtpModel, String> {
    @Query("{'user.email' = ?0 }")
    public Optional<OtpModel> findByEmail(String email);
}

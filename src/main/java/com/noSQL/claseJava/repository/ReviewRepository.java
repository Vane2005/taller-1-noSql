package com.noSQL.claseJava.repository;

import com.noSQL.claseJava.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByUserUserId(String userId);
    List<Review> findByBookBookId(String bookId);
    Optional<Review> findByUserUserIdAndBookBookId(String userId, String bookId);
    void deleteByUserUserId(String userId);
    void deleteByBookBookId(String bookId);
}

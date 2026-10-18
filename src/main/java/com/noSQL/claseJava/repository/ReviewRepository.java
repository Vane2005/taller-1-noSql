package com.noSQL.claseJava.repository;

import com.noSQL.claseJava.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByUserUserId(String userId);
    List<Review> findByBookBookId(String bookId);
    void deleteByUserUserId(String userId);
    void deleteByBookBookId(String bookId);
}

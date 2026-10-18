package com.noSQL.claseJava.repository;

import com.noSQL.claseJava.entity.Favorite;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FavoriteRepository extends MongoRepository<Favorite, String> {
    List<Favorite> findByUserId(String userId);
    List<Favorite> findByFavoriteId(String bookId);
    void deleteByUserId(String userId);
    void deleteByFavoriteId(String bookId);
}

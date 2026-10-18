package com.noSQL.claseJava.repository;

import com.noSQL.claseJava.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findByAuthorFullName(String fullName);

    List<Book> findByEditorialName(String editorialName);

    List<Book> findByCategoriesName(String categoryName);

    List<Book> findByAvailable(Boolean available);

    List<Book> findByPublicationYear(Integer year);

    List<Book> findByTitleContainingIgnoreCase(String title);
}

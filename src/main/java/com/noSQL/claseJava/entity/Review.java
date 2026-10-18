package com.noSQL.claseJava.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document(collection = "reviews")
public class Review implements Serializable {

    @Id
    private String id;
    private String rating;
    private String feedback;
    private userReference user;
    private bookReference book;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class userReference{
        private String userId;
        private String name;
        private String email;
        private String telephone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class bookReference{
        private String bookId;
        private String title;
        private authorReference author;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class authorReference{
        private String authorId;
        private String name;
        private String nationality;
    }

}

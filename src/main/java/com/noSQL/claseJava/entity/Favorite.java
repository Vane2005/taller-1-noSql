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
@Document(collection = "favorites")
public class Favorite implements Serializable {

    @Id
    private String id;
    private userReference user;
    private favoriteList favorite;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class userReference{
        private String id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class favoriteList{
        private String id;
        private String title;
        private authorReference author;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class authorReference{
        private String id;
        private String fullName;
    }
}

package com.noSQL.claseJava.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document(collection = "books")
public class Book implements Serializable {

    @Id
    private String id;
    private String uniqueIdentifier;
    private String title;
    private String synopsis;
    private Integer publicationYear;
    private Boolean available;
    private Author author;
    private Editorial editorial;
    private List<Category> categories;
    private Float averageCalifications;
    private Integer reviewsTotal;
    private Boolean isLent;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Author {
        private String id;
        private String fullName;
        private String nationality;
        private String biography;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Editorial {
        private String id;
        private String name;
        private String country;
        private String website;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Category {
        private String id;
        private String name;
        private String description;
    }
}

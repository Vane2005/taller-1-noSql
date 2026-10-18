package com.noSQL.claseJava.dto;

import lombok.*;
import com.noSQL.claseJava.entity.Book.Author;
import com.noSQL.claseJava.entity.Book.Editorial;
import com.noSQL.claseJava.entity.Book.Category;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookRequest {

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
}

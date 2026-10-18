package com.noSQL.claseJava.dto;

import com.noSQL.claseJava.entity.Author.BookReference;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AuthorRequest {

    private String fullName;
    private String nationality;
    private String biography;
    private List<BookReference> books;

}

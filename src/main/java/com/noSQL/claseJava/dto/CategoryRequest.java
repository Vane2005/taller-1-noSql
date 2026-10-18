package com.noSQL.claseJava.dto;

import com.noSQL.claseJava.entity.Category;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CategoryRequest {

    private String name;
    private String description;
    private List<Category.BookReference> books;

}

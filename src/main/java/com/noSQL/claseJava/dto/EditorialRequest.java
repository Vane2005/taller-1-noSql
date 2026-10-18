package com.noSQL.claseJava.dto;

import com.noSQL.claseJava.entity.Editorial;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EditorialRequest {

    private String name;
    private String country;
    private String website;
    private List<Editorial.BookReference> books;
}

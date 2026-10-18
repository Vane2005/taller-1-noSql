package com.noSQL.claseJava.dto;

import com.noSQL.claseJava.entity.Review.bookReference;
import com.noSQL.claseJava.entity.Review.userReference;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReviewRequest {

    private String rating;
    private String feedback;
    private userReference user;
    private bookReference book;

}

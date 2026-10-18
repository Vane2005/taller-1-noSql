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
@Document(collection = "user")
public class User implements Serializable {

    @Id
    private String id;
    private String name;
    private String address;
    private String email;
    private String telephone;

     @Builder.Default
    private List<activityRef> resumeActivity = new java.util.ArrayList<>(5);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class activityRef{
        private String activityId;
        private String nameActivity;
    }

}

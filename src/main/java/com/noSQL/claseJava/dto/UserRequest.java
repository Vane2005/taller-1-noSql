package com.noSQL.claseJava.dto;

import com.noSQL.claseJava.entity.User.activityRef;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserRequest {

    private String id;
    private String name;
    private String address;
    private String email;
    private String telephone;

    private List<activityRef> resumeActivity;

}

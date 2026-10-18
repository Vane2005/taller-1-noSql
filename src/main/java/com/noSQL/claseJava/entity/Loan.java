package com.noSQL.claseJava.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document(collection = "loans")
public class Loan implements Serializable {

    @Id
    private String id;
    private String loandDate;
    private String loanEndDate;
    private String loanReturnRealDate;
    private String state;
    private bookReference bookLoan;
    private userReference userLoan;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class bookReference{
        private String id;
        private String title;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class userReference{
        private String id;
        private String name;
        private String email;
        private String telephone;
    }
}

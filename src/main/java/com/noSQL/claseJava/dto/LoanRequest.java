package com.noSQL.claseJava.dto;

import com.noSQL.claseJava.entity.Loan.bookReference;
import com.noSQL.claseJava.entity.Loan.userReference;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoanRequest {

    private String loandDate;
    private String loanEndDate;
    private String loanReturnRealDate;
    private String state;
    private bookReference bookLoan;
    private userReference userLoan;

}

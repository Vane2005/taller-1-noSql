package com.noSQL.claseJava.repository;

import com.noSQL.claseJava.entity.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoanRepository extends MongoRepository<Loan, String> {
    List<Loan> findByUserLoanId(String userId);
    List<Loan> findByBookLoanId(String bookId);
    List<Loan> findByState(String state);
    void deleteByUserLoanId(String userId);
    void deleteByBookLoanId(String bookId);
}

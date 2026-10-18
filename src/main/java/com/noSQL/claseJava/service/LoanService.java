package com.noSQL.claseJava.service;

import com.noSQL.claseJava.dto.LoanRequest;
import com.noSQL.claseJava.entity.Loan;
import com.noSQL.claseJava.entity.User;
import com.noSQL.claseJava.repository.LoanRepository;
import com.noSQL.claseJava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public Loan createLoan(LoanRequest request) {
        Loan loan = Loan.builder()
                .loandDate(request.getLoandDate())
                .loanEndDate(request.getLoanEndDate())
                .loanReturnRealDate(request.getLoanReturnRealDate())
                .state(request.getState())
                .bookLoan(request.getBookLoan())
                .userLoan(request.getUserLoan())
                .build();
        
        loan = loanRepository.save(loan);

        if (loan.getUserLoan() != null && loan.getUserLoan().getId() != null) {
            addLoanToUserActivity(loan.getUserLoan().getId(), loan.getId());
        }

        return loan;
    }

    // Funcion auxiliar para añadir la actividad al user
    private void addLoanToUserActivity(String userId, String loanId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getResumeActivity() == null) {
                user.setResumeActivity(new java.util.ArrayList<>());
            }
            user.getResumeActivity().add(User.activityRef.builder()
                    .activityId(loanId)
                    .nameActivity("loan")
                    .build());
            userRepository.save(user);
        }
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(String id) {
        return loanRepository.findById(id);
    }

    public List<Loan> getLoansByUserId(String userId) {
        return loanRepository.findByUserLoanId(userId);
    }

    public List<Loan> getLoansByBookId(String bookId) {
        return loanRepository.findByBookLoanId(bookId);
    }

    public List<Loan> getLoansByState(String state) {
        return loanRepository.findByState(state);
    }

    public Loan updateLoan(String id, LoanRequest request) {
        Optional<Loan> existingLoan = loanRepository.findById(id);
        if (existingLoan.isPresent()) {
            Loan loan = existingLoan.get();
            loan.setLoandDate(request.getLoandDate());
            loan.setLoanEndDate(request.getLoanEndDate());
            loan.setLoanReturnRealDate(request.getLoanReturnRealDate());
            loan.setState(request.getState());
            loan.setBookLoan(request.getBookLoan());
            loan.setUserLoan(request.getUserLoan());
            return loanRepository.save(loan);
        }
        return null;
    }

    public boolean deleteLoan(String id) {
        Optional<Loan> loan = loanRepository.findById(id);
        if (loan.isPresent()) {
            Loan l = loan.get();
            // Remove activity from user if necessary
            if (l.getUserLoan() != null && l.getUserLoan().getId() != null) {
                removeLoanFromUserActivity(l.getUserLoan().getId(), id);
            }
            loanRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void removeLoanFromUserActivity(String userId, String loanId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getResumeActivity() != null) {
                user.getResumeActivity().removeIf(activity -> loanId.equals(activity.getActivityId()));
                userRepository.save(user);
            }
        }
    }
}

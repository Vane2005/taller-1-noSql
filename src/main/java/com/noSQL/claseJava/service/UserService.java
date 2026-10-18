package com.noSQL.claseJava.service;

import com.noSQL.claseJava.dto.UserRequest;
import com.noSQL.claseJava.entity.User;
import com.noSQL.claseJava.repository.LoanRepository;
import com.noSQL.claseJava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;

    public User createUser(UserRequest request){

        User user = User.builder()
                .id(request.getId())
                .name(request.getName())
                .email(request.getEmail())
                .address(request.getAddress())
                .telephone(request.getTelephone())
                .build();
        return userRepository.save(user);
    }

    public List<User.activityRef> getResumeById(String id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User user1 = user.get();
            if(!user1.getResumeActivity().isEmpty()){
                return user1.getResumeActivity();
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByTelephone(String telephone){
        return userRepository.findByTelephone(telephone);
    }

    public Optional<User> getUserByName(String name){
        return userRepository.findByName(name);
    }

    public User updateUser(String idUser, UserRequest request){
        Optional<User> findUser = userRepository.findById(idUser);
        if(findUser.isPresent()){
            User user = findUser.get();

            String oldName = user.getName();
            String oldEmail = user.getEmail();
            String oldTelephone = user.getTelephone();

            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setTelephone(request.getTelephone());
            user.setAddress(request.getAddress());

            User updatedUser = userRepository.save(user);

            if (!oldName.equals(request.getName()) || !oldEmail.equals(request.getEmail()) || !oldTelephone.equals(request.getTelephone())) {
                //Actualizar en Loan
                updateUserInLoan(user.getId(), request.getName(), request.getEmail(), request.getTelephone());

                // Actualizar en Review
                updateUserInReview(user.getId(), request.getName(), request.getEmail(), request.getTelephone());

                // Actualizar en Favorites
                updateNameUserInFavorites(user.getId(), request.getName(), request.getEmail(), request.getTelephone());
            }

            return updatedUser;
        }
        return null;
    }

    // Funciones auxiliares para actualizaciones
    public void updateUserInLoan(String userId, String newName, String newEmail, String newTelephone){
        loanRepository.findByUserLoanId(userId).forEach(loan -> {
            if (loan.getUserLoan() != null) {
                loan.getUserLoan().setName(newName);
                loan.getUserLoan().setEmail(newEmail);
                loan.getUserLoan().setTelephone(newTelephone);
                loanRepository.save(loan);
            }
        });
    }

    public void updateUserInReview(String userId, String newName, String newEmail, String newTelephone){

    }

    public void updateNameUserInFavorites(String userId, String newName, String newEmail, String newTelephone){

    }

    public boolean deleteUser(String idUser){
        Optional<User> findUser = userRepository.findById(idUser);
        if(findUser.isPresent()){
            User user =  findUser.get();

            //Eliminar user en review

            //Eliminar user en Loan
            loanRepository.deleteByUserLoanId(idUser);

            //Eliminar user en favorites

            userRepository.deleteById(idUser);
            return true;
        }
        return false;
    }



}

package com.noSQL.claseJava.controllers;

import com.noSQL.claseJava.dto.UserRequest;
import com.noSQL.claseJava.entity.User;
import com.noSQL.claseJava.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/resume/{id}")
    public ResponseEntity<List<User.activityRef>> getActivityResume(@PathVariable String id) {
        List<User.activityRef> resume = userService.getResumeById(id);

        if (resume == null) {
            return ResponseEntity.notFound().build();
        }
        if (resume.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resume);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }

    @GetMapping("/serchbyemail/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }

    @GetMapping("/searchbyphone/{telephone}")
    public ResponseEntity<User> getUserByTelephone(@PathVariable String telephone) {
        Optional<User> user = userService.getUserByTelephone(telephone);
        return user.map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }

    @GetMapping("/searchbyname/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        Optional<User> user = userService.getUserByName(name);
        return user.map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequest request) {
        User userCreated = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserRequest request, @PathVariable String id) {
        User updatedUser = userService.updateUser(id, request);
        if(updatedUser != null){
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        boolean eliminated = userService.deleteUser(id);
        if(eliminated){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}

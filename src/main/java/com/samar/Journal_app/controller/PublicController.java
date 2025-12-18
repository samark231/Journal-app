package com.samar.Journal_app.controller;

import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("public")
public class PublicController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user){
        Boolean saved = userService.saveUser(user);
        if(saved) return new ResponseEntity<>("user created", HttpStatus.CREATED);
        return new ResponseEntity<>("error while creating user.", HttpStatus.BAD_REQUEST);
    }
}

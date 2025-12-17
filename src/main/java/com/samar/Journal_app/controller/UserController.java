package com.samar.Journal_app.controller;

import com.samar.Journal_app.dto.PasswordChangeRequest;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.service.UserService;
import com.samar.Journal_app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public ResponseEntity<?> greeting(Authentication authentication){
        int temp = weatherService.getFeelsLike("sherghati");
        return new ResponseEntity<>("Hi "+authentication.getName()+" Today Weather feels like " + temp, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestBody PasswordChangeRequest dtoPasswords){
        String username = authentication.getName();
        if(dtoPasswords.getOldPassword()==null || dtoPasswords.getNewPassword()==null) {
            return new ResponseEntity<>("send old and new password both", HttpStatus.BAD_REQUEST);
        }
        User user =  userService.getUserByUsername(username);
        if(passwordEncoder.matches(dtoPasswords.getOldPassword(), user.getPassword())){
            userService.updatePassword(username, dtoPasswords.getNewPassword());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("password did not match", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("delete-user")
    public ResponseEntity<?> deleteUser(Authentication authentication, @RequestBody Map<String,String> pass){
        String username = authentication.getName();
        User currentUser = userService.getUserByUsername(username);
//        System.out.println("password is "+pass);
        if(passwordEncoder.matches(pass.get("password"), currentUser.getPassword())){
            userService.deleteUser(currentUser);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("password did not match", HttpStatus.BAD_REQUEST);
        }
    }


}

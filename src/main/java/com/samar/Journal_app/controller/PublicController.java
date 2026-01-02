package com.samar.Journal_app.controller;

import com.samar.Journal_app.dto.UserLogInRequest;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.service.UserDetailsServiceImpl;
import com.samar.Journal_app.service.UserService;
import com.samar.Journal_app.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("public")
public class PublicController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> Signup(@RequestBody User user){
        Boolean saved = userService.saveUser(user);
        if(saved) return new ResponseEntity<>("user created", HttpStatus.CREATED);
        return new ResponseEntity<>("error while creating user.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogInRequest user){
        try{
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsernameOrEmail(), user.getPassword()));
            String jwt = jwtUtils.generateToken(authenticate.getName());
            Map<String, Object> response = new HashMap<>();
            User userData = userService.getUserByUsername(authenticate.getName());
            response.put("token", jwt);
            response.put("user", userData);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            log.error("Error occured while trying to log in: ",e);
            return new ResponseEntity<>("Bad Credentialss", HttpStatus.BAD_REQUEST);
        }
    }

}

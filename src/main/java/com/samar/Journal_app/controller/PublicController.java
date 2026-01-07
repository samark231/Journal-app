package com.samar.Journal_app.controller;

import com.samar.Journal_app.dto.UserLogInRequest;
import com.samar.Journal_app.dto.UserSignUpDto;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.response.ApiResponse;
import com.samar.Journal_app.service.UserDetailsServiceImpl;
import com.samar.Journal_app.service.UserService;
import com.samar.Journal_app.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
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
@RequiredArgsConstructor
@RequestMapping("public")
public class PublicController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @GetMapping("/health-check")
    public ResponseEntity<ApiResponse<String>> healthCheck(){
        ApiResponse<String> response = new ApiResponse<>(true, "Health check pass", "OK");
    "s
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> Signup(@RequestBody @Valid UserSignUpDto signUpRequest){
        User savedUser = userService.saveUser(signUpRequest);
        ApiResponse<User> response = new ApiResponse<>(true, "User Created.", savedUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
            return new ResponseEntity<>("Bad Credentials", HttpStatus.BAD_REQUEST);
        }
    }

}

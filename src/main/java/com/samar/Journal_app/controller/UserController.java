package com.samar.Journal_app.controller;

import com.samar.Journal_app.dto.EmailDto;
import com.samar.Journal_app.dto.PasswordChangeRequest;
import com.samar.Journal_app.dto.UserDto;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.response.ApiResponse;
import com.samar.Journal_app.service.EmailService;
import com.samar.Journal_app.service.UserService;
import com.samar.Journal_app.service.WeatherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<ApiResponse<Integer>> greeting(Authentication authentication){
        int temp = weatherService.getFeelsLike("sherghati");
        ApiResponse<Integer> response = new ApiResponse<>(true, "weather fetched successfully", temp);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@RequestBody UserDto userDto, Authentication authentication){
        log.info("A request has reached updateUser controller.");
        UserDto updatedUser = userService.updateUserDetails(authentication.getName(), userDto);
        ApiResponse<UserDto> userDtoApiResponse = new ApiResponse<>(true,  "user updated successfully", updatedUser);
        return new ResponseEntity<>(userDtoApiResponse, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestBody @Valid PasswordChangeRequest dtoPasswords){
        userService.updatePassword(authentication.getName(), dtoPasswords);
        return new ResponseEntity<>(new ApiResponse<>(true, "password updated."), HttpStatus.OK);
    }

    @DeleteMapping("delete-user")
    public ResponseEntity<ApiResponse<Boolean>> deleteUser(Authentication authentication, @RequestBody Map<String,String> pass){
        userService.deleteUser(authentication.getName(), pass);
        return new ResponseEntity<>(new ApiResponse<>(true, "user deleted successfully",true), HttpStatus.OK);
    }

    @GetMapping("check-auth")
    public ResponseEntity<ApiResponse<User>> checkAuthentication(Authentication authentication){
        User user = userService.getUserByUsername(authentication.getName());
        return new ResponseEntity<>(new ApiResponse<>(true, "user authenticated successfully", user), HttpStatus.OK);
    }

    @PostMapping("send-email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailDto emailDto){
        if(emailDto.getTo()==null || emailDto.getSubject()==null|| emailDto.getBody()==null){
            return new ResponseEntity<>("all three fields- to, subject and body are necesaary.", HttpStatus.BAD_REQUEST);
        }else {
            Boolean sent = emailService.sendEmail(emailDto.getTo(), emailDto.getSubject(), emailDto.getBody());
            if(sent){
                return new ResponseEntity<>("mail sent successfully.", HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>("some error occured", HttpStatus.NOT_FOUND);
            }
        }
    }



}

package com.samar.Journal_app.service;

import com.samar.Journal_app.config.CustomUserDetails;
import com.samar.Journal_app.dto.UpdateUserDto;
import com.samar.Journal_app.dto.UserLogInRequest;
import com.samar.Journal_app.dto.UserSignUpDto;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.repository.JournalEntryRepository;
import com.samar.Journal_app.repository.UserRepository;
import com.samar.Journal_app.repository.UserRepositoryImpl;
import com.samar.Journal_app.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepositoryImpl userRepositoryImpl;
    private JournalEntryRepository journalEntryRepository;
    private UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public User saveUser(UserSignUpDto signUpDto){
        try {
            User user = User.builder()
                    .firstName(signUpDto.getFirstName())
                    .lastName(signUpDto.getLastName())
                    .username(signUpDto.getUsername())
                    .email(signUpDto.getEmail())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .roles(Arrays.asList("USER"))
                    .build();
            return userRepository.save(user);

        }catch (Exception e){
            log.error("error occurred in userService while performing saveUser:", e);
            return null;
        }
    }
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public void updatePassword(String username, String newPassword){
        userRepository.updatePassword(username, passwordEncoder.encode(newPassword));
    }
    public void addJournalEntryToUser(String username, ObjectId id){
        userRepository.addJournalId(username, id);
    }
    public Long deleteJournalEntryFromUser(String username, ObjectId id){
        return userRepository.removeJournalId(username, id);
    }

    @Transactional
    public void deleteUser(User user){
            journalEntryRepository.deleteAll(user.getJournalEntries());
            userRepository.delete(user);
    }
    public Long deleteAllUsers(){
           return userRepositoryImpl.deleteAllUsers();
    }

    public Boolean updateEmailAndSentiment(String username, String email, Boolean sentiment){
            return userRepositoryImpl.updateEmailAndSentiment(username, email, sentiment);
    }
    public Map<String , Object> logInUser(UserLogInRequest user){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsernameOrEmail(), user.getPassword()));
        String jwt = jwtUtils.generateToken(authentication.getName());
        User userData = getUserFromAuth(authentication);
        Map<String, Object > response = new HashMap<>();
        response.put("jwt", jwt);
        response.put("userData", userData);
        log.info("log in request reached loginUser service:");
        return response;
    }
    public User getUserFromAuth(Authentication authentication){
            Object obj = authentication.getPrincipal();
            CustomUserDetails userDetails = (CustomUserDetails) obj;
            return userDetails.getUserEntity();
    }

}

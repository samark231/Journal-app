package com.samar.Journal_app.service;

import com.samar.Journal_app.config.CustomUserDetails;
import com.samar.Journal_app.dto.PasswordChangeRequest;
import com.samar.Journal_app.dto.UserDto;
import com.samar.Journal_app.dto.UserLogInRequest;
import com.samar.Journal_app.dto.UserSignUpDto;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.exception.UserNotUpdated;
import com.samar.Journal_app.repository.JournalEntryRepository;
import com.samar.Journal_app.repository.UserRepository;
import com.samar.Journal_app.repository.UserRepositoryImpl;
import com.samar.Journal_app.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryImpl userRepositoryImpl;
    private final JournalEntryRepository journalEntryRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public User saveUser(UserSignUpDto signUpDto){
        User user = User.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .username(signUpDto.getUsername())
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .roles(Arrays.asList("USER"))
                .build();
        return userRepository.save(user);
    }
    public List<User> getAllUser(){
        return userRepository.findAll();
    }
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public void updatePassword(String username, PasswordChangeRequest passwordDto){
        String encodedPassword = userRepository.findByUsername(username).getPassword();
        if(passwordEncoder.matches(passwordDto.getOldPassword(),encodedPassword)){
            userRepository.updatePassword(username, passwordEncoder.encode(passwordDto.getNewPassword()));
        }else{
            throw new BadCredentialsException("Wrong password. Try again...");
        }

    }
    public void addJournalEntryToUser(String username, ObjectId id){
        userRepository.addJournalId(username, id);
    }
    public Long deleteJournalEntryFromUser(String username, ObjectId id){
        return userRepository.removeJournalId(username, id);
    }
    public UserDto updateUserDetails(String username, UserDto oldUserDetails){
        User updatedUser = userRepositoryImpl.updateUserDetails(username, oldUserDetails);
        if(updatedUser!=null){
            return UserDto.builder()
                    .firstName(updatedUser.getFirstName())
                    .lastName(updatedUser.getLastName())
                    .email(updatedUser.getEmail())
                    .dob(updatedUser.getDob())
                    .gender(updatedUser.getGender())
                    .build();
        }else{
            throw new UserNotUpdated("Could not update user. Check details again or try after some time.");
        }
    }
    @Transactional
    public void deleteUser(String username , Map<String, String> pass){
            User currentUser = getUserByUsername(username);
            if(passwordEncoder.matches(pass.get("password"),currentUser.getPassword())){
                journalEntryRepository.deleteAll(currentUser.getJournalEntries());
                userRepository.delete(currentUser);
            }else{
                throw new BadCredentialsException("Password did not match, try again");
            }
    }
    public Long deleteAllUsers(){
           return userRepositoryImpl.deleteAllUsers();
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

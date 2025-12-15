package com.samar.Journal_app.service;

import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

        public boolean saveUser(User user){
        try {
            userRepository.save(user);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            return  true;
        }catch (Exception e){
            log.error("error occurred in userService while performing saveTestUser:");
            log.warn("error occurred in userService while performing saveTestUser:");
            log.info("error occurred in userService while performing saveTestUser:");
            log.debug("error occurred in userService while performing saveTestUser:");
            log.trace("error occurred in userService while performing saveTestUser:");
            return false;
        }
    }
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUserByUsername(String username){
//        System.out.println("username is "+username);
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
    public void deleteUser(User user){
        userRepository.delete(user);
    }

}

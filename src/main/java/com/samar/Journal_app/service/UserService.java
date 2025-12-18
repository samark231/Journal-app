package com.samar.Journal_app.service;

import com.samar.Journal_app.dto.UpdateUserDto;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.repository.JournalEntryRepository;
import com.samar.Journal_app.repository.UserRepository;
import com.samar.Journal_app.repository.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserRepository userRepository;

        public boolean saveUser(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return  true;
        }catch (Exception e){
            log.error("error occurred in userService while performing saveTestUser:");
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

}

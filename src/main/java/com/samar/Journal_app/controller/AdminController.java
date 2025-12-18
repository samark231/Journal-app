package com.samar.Journal_app.controller;

import com.samar.Journal_app.cache.AppCache;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.service.JournalEntryService;
import com.samar.Journal_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private AppCache appCache;

    @GetMapping("all-users")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }
    @GetMapping("all-journals")
    public ResponseEntity<?> getAllJournals(){
        return new ResponseEntity<>(journalEntryService.getAllJournals(), HttpStatus.OK);
    }
    @GetMapping("clear-cache")
    public void clearAppCache(){
        appCache.init();
    }

    @DeleteMapping("del-all-users")
    public ResponseEntity<?> deleteAllUser(){
        Long delCount =  userService.deleteAllUsers();
        if(delCount>0) return  new ResponseEntity<>("no. of deleted users: "+delCount,HttpStatus.OK);
        return new ResponseEntity<>("no users found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("del-all-journals")
    public boolean deleteAllJournals(){
        journalEntryService.deleteAll();
        return true;
    }


}

package com.samar.Journal_app.controller;

import com.samar.Journal_app.cache.AppCache;
import com.samar.Journal_app.entity.JournalEntry;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.response.ApiResponse;
import com.samar.Journal_app.service.JournalEntryService;
import com.samar.Journal_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final JournalEntryService journalEntryService;
    private final AppCache appCache;

    @GetMapping("all-users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(){
        List<User> users = userService.getAllUser();
        ApiResponse<List<User>> response = new ApiResponse<>(true, "fetching all users successful", users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("all-journals")
    public ResponseEntity<ApiResponse<List<JournalEntry> >> getAllJournals(){
        List<JournalEntry> allJournals = journalEntryService.getAllJournals();
        ApiResponse<List<JournalEntry> > response = new ApiResponse<>(true, "fetching all Journals successful", allJournals);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("clear-cache")
    public void clearAppCache(){
        appCache.init();
    }

    @DeleteMapping("del-all-users")
    public ResponseEntity<ApiResponse<?>> deleteAllUser(){
        Long delCount =  userService.deleteAllUsers();
        ApiResponse<?> response = new ApiResponse<>(true, delCount+" users deleted", null);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("del-all-journals")
    public ResponseEntity<ApiResponse<?>> deleteAllJournals(){
        journalEntryService.deleteAll();
        ApiResponse<?> response = new ApiResponse<>(true, "All Journals deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

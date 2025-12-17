package com.samar.Journal_app.controller;

import com.samar.Journal_app.cache.AppCache;
import com.samar.Journal_app.entity.User;
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
    private AppCache appCache;

    @GetMapping("all-users")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }
    @GetMapping("clear-cache")
    public void clearAppCache(){
        appCache.init();
    }


}

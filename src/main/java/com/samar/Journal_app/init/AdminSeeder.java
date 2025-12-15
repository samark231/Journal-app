package com.samar.Journal_app.init;

import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AdminSeeder {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void onAppReady(){
        if(userRepository.checkAdmin("ADMIN")){
            return;
        }
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(admin);
        System.out.println("default admin user created...");
    }
}

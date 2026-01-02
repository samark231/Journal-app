package com.samar.Journal_app.service;

import com.samar.Journal_app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException{
        List<User> users = userService.getUserByUsernameOrEmail(identifier);

        if(users.size()==1) {
            User user = users.get(0);
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }else if (users.isEmpty()){
            throw new UsernameNotFoundException("user not found with the username: "+ identifier);
        }else{
            throw new UsernameNotFoundException("mulitple users found with this email: "+ identifier);
        }

    }
}

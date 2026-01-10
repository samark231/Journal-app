package com.samar.Journal_app.service;

import com.samar.Journal_app.config.CustomUserDetails;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepositoryImpl userRepositoryImpl;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException{
        List<User> users = userRepositoryImpl.getUserByUsernameOrEmail(identifier);

        if(users.size()==1) {
            User user = users.get(0);
            return new CustomUserDetails(user);
        }else if (users.isEmpty()){
            throw new UsernameNotFoundException("user not found with the username: "+ identifier);
        }else{
            throw new UsernameNotFoundException("mulitple users found with this email: "+ identifier);
        }

    }
}

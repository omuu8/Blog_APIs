package com.example.blog_app.demo_blogging.security;

import com.example.blog_app.demo_blogging.exception.ResourceNotFoundException;
import com.example.blog_app.demo_blogging.model.User;
import com.example.blog_app.demo_blogging.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException {

        //Loading user from database using username
        User user = this.userRepo.findByEmail(username)
                .orElseThrow(()-> new ResourceNotFoundException("User with username not found","email : "+username,0));

        return user;
    }
}

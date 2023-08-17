package com.example.blog_app.demo_blogging.controller;

import com.example.blog_app.demo_blogging.exception.ApiException;
import com.example.blog_app.demo_blogging.payload.JwtAuthRequest;
import com.example.blog_app.demo_blogging.payload.JwtAuthResponse;
import com.example.blog_app.demo_blogging.payload.UserDto;
import com.example.blog_app.demo_blogging.repo.UserRepo;
import com.example.blog_app.demo_blogging.security.JwtTokenHelper;
import com.example.blog_app.demo_blogging.service.UserService;
import com.example.blog_app.demo_blogging.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogs/api/v1/auth/")

public class AuthController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationmanager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request) throws Exception{

        //First we authenticate the request's username and password
        this.authenticate(request.getUsername(),request.getPassword());

        //we will fetch the userDetails by using userDetailService
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

        //We generate the token with the help of jwtTokenHelper class
        String token = this.jwtTokenHelper.generateToken(userDetails);

        //create authResponse and set the token which returned later
        JwtAuthResponse response = JwtAuthResponse.builder().token(token)
                .userDto(this.mapper.map((User)userDetails,UserDto.class)).build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    private void authenticate(String username, String password) throws Exception {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
        try {
            this.authenticationmanager.authenticate(token);
        }catch(DisabledException e) {
            System.out.println("User is disabled");
        }catch(BadCredentialsException e) {
            System.out.println("Invalid Details");
            throw new ApiException("Invalid username or password!");
        }
    }

    // Register newuser Api
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){

        UserDto registeredUser = this.userService.registerUser(userDto);

        return new ResponseEntity<UserDto>(registeredUser,HttpStatus.CREATED);

    }

}

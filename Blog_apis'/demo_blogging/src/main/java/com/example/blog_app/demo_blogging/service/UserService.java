package com.example.blog_app.demo_blogging.service;

import com.example.blog_app.demo_blogging.payload.UserDto;

import java.util.List;

public interface UserService {

    UserDto registerUser(UserDto userDto);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto,Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void deleteUser(Integer userId);
    void deleteAll();

}

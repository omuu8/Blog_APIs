package com.example.blog_app.demo_blogging.service.impl;

import com.example.blog_app.demo_blogging.config.AppConstants;
import com.example.blog_app.demo_blogging.exception.ResourceNotFoundException;
import com.example.blog_app.demo_blogging.model.Role;
import com.example.blog_app.demo_blogging.model.User;
import com.example.blog_app.demo_blogging.payload.UserDto;
import com.example.blog_app.demo_blogging.repo.RoleRepo;
import com.example.blog_app.demo_blogging.repo.UserRepo;
import com.example.blog_app.demo_blogging.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;

    @Override
    public UserDto registerUser(UserDto userDto) {

        User user = this.modelMapper.map(userDto,User.class);

        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        Role role =this.roleRepo.findById(AppConstants.NORMAL_USER).get();

        user.getRoles().add(role);

        User newUser =this.userRepo.save(user);

        return this.modelMapper.map(newUser, UserDto.class);

    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto,User.class);
        User savedUser = this.userRepo.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));
        if(userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if(userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if(userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }
        if(userDto.getAbout() != null) {
            user.setAbout(userDto.getAbout());
        }

        User updatedUser = this.userRepo.save(user);

        return this.modelMapper.map(updatedUser, UserDto.class );
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));

        return this.modelMapper.map(user,UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {

       List<User> users =this.userRepo.findAll();

       return users.stream().map(u -> this.modelMapper.map(u, UserDto.class)).toList();
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));

        this.userRepo.delete(user);
    }

    @Override
    public void deleteAll() {

        this.userRepo.deleteAll();
    }
}

package com.example.blog_app.demo_blogging.controller;

import com.example.blog_app.demo_blogging.payload.ApiResponse;
import com.example.blog_app.demo_blogging.payload.UserDto;
import com.example.blog_app.demo_blogging.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/blogs/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    // ResponseEntity allows user to define any particular response in exchange of any request
    //along with response such as 200,404 you can also customize the headers and response body

    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto createUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }
    //PUT - Update user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable("userId") Integer uid){
        UserDto updatedUser = this.userService.updateUser(userDto, uid);
        return ResponseEntity.ok(updatedUser);
    }

    //GET - Get User
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable("userId") Integer uid){

        return ResponseEntity.ok(this.userService.getUserById(uid));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(this.userService.getAllUsers());

    }
    // Only Admin can have access to delete the user or not

    //Delete- Delete User
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId){
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully",true),HttpStatus.OK);
    }
    @DeleteMapping
    public ResponseEntity<?> deleteAllUsers(){
        this.userService.deleteAll();
        return new ResponseEntity<>(new ApiResponse("All Users Deleted Successfully",true),HttpStatus.OK);
    }
    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> patchUser(@Valid @RequestBody UserDto userDto,@PathVariable("userId") Integer uid){
        UserDto patchedUser = this.userService.updateUser(userDto, uid);
        return ResponseEntity.ok(patchedUser);
    }
}

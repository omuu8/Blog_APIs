package com.example.blog_app.demo_blogging.controller;

import com.example.blog_app.demo_blogging.payload.ApiResponse;
import com.example.blog_app.demo_blogging.payload.CommentDto;
import com.example.blog_app.demo_blogging.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blogs/api")
public class CommentController {

    @Autowired
    private CommentService service;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
                                                    @PathVariable("postId") Integer postId
    ){
        CommentDto cDtoCreated = this.service.createComment(commentDto, postId);
        return new ResponseEntity<>(cDtoCreated, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable("commentId") Integer commentId){
        this.service.deleteComment(commentId);
        return new ResponseEntity(new ApiResponse("Comment Deleted Successfully !",true),HttpStatus.OK);
    }

}

package com.example.blog_app.demo_blogging.service;

import com.example.blog_app.demo_blogging.payload.CommentDto;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto, Integer postId);

    void deleteComment(Integer commentId);
}

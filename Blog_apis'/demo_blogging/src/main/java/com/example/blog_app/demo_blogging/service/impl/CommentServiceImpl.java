package com.example.blog_app.demo_blogging.service.impl;

import com.example.blog_app.demo_blogging.exception.ResourceNotFoundException;
import com.example.blog_app.demo_blogging.model.Comment;
import com.example.blog_app.demo_blogging.model.Post;
import com.example.blog_app.demo_blogging.payload.CommentDto;
import com.example.blog_app.demo_blogging.repo.CommentRepo;
import com.example.blog_app.demo_blogging.repo.PostRepo;
import com.example.blog_app.demo_blogging.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        Post p = this.postRepo.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","post_id",postId));

        Comment comment = this.modelMapper.map(commentDto,Comment.class);

        comment.setPost(p);

        Comment savedComment= this.commentRepo.save(comment);

        return this.modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","comment_id",commentId));

        this.commentRepo.delete(comment);
    }
}

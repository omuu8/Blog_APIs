package com.example.blog_app.demo_blogging.service;

import com.example.blog_app.demo_blogging.payload.PostDto;
import com.example.blog_app.demo_blogging.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

    PostDto updatePost(PostDto postDto,Integer postId);

    void deletePost(Integer postId);

    PostResponse getAllPosts(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    PostDto getPost(Integer postId);


    PostResponse getPostsByUser(Integer userId, Integer pageNo, Integer pageSize);

    PostResponse getPostsByCategory(Integer categoryId, Integer pageNo, Integer pageSize);

    List<PostDto> searchPosts(String keyword);
}

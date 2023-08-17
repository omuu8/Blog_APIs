package com.example.blog_app.demo_blogging.service.impl;

import com.example.blog_app.demo_blogging.exception.ResourceNotFoundException;
import com.example.blog_app.demo_blogging.model.Category;
import com.example.blog_app.demo_blogging.model.Post;
import com.example.blog_app.demo_blogging.payload.PostDto;
import com.example.blog_app.demo_blogging.payload.PostResponse;
import com.example.blog_app.demo_blogging.model.User;
import com.example.blog_app.demo_blogging.repo.CategoryRepo;
import com.example.blog_app.demo_blogging.repo.PostRepo;
import com.example.blog_app.demo_blogging.repo.UserRepo;
import com.example.blog_app.demo_blogging.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;
    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","User Id",userId));

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category ID",categoryId));

        Post post = this.modelMapper.map(postDto,Post.class);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        Post newPost = this.postRepo.save(post);

        return this.modelMapper.map(newPost, PostDto.class);


    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {

        Post post = this.postRepo.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","post id",postId));

        if(postDto.getTitle() != null) {
            post.setTitle(postDto.getTitle());
        }
        if(postDto.getImageName() != null) {
            post.setImageName(postDto.getImageName());
        }
        if(postDto.getContent() != null) {
            post.setContent(postDto.getContent());
        }
        Post updatedPost = this.postRepo.save(post);
        return this.modelMapper.map(updatedPost,PostDto.class);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","post id",postId));
        this.postRepo.delete(post);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNo,pageSize,sort);

        Page p = this.postRepo.findAll(page);

        List<Post> posts = p.getContent();

        List<PostDto> postDtos = posts.stream().map(po -> this.modelMapper.map(po,PostDto.class)).toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setPosts(postDtos);
        postResponse.setPageNo(p.getNumber());
        postResponse.setPageSize(p.getSize());
        postResponse.setTotalElements(p.getTotalElements());
        postResponse.setTotalPages(p.getTotalPages());
        postResponse.setLastPage(p.isLast());

        return postResponse;


    }

    @Override
    public PostDto getPost(Integer postId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","Post Id",postId));

        return this.modelMapper.map(post,PostDto.class);
    }

    @Override
    public PostResponse getPostsByUser(Integer userId, Integer pageNo, Integer pageSize) {

        //Here the Pageable interface helps in configuring how the data you want to showcase
        //in your output

        Pageable page = PageRequest.of(pageNo,pageSize);

        User user = this.userRepo.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User","User Id",userId));

        Page<Post> pages = this.postRepo.findAllByUser(user,page);

        List<PostDto> postDtos = pages.map(p -> this.modelMapper.map(p, PostDto.class)).getContent();

        PostResponse response = new PostResponse();
        response.setPosts(postDtos);
        response.setPageNo(pageNo);
        response.setPageSize(pageSize);
        response.setTotalElements(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        response.setLastPage(pages.isLast());

        return response;


    }


    @Override
    public PostResponse getPostsByCategory(Integer categoryId, Integer pageNo, Integer pageSize) {
        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category Id",categoryId));

        Pageable p = PageRequest.of(pageNo,pageSize);

        Page<Post> pages = this.postRepo.findAllByCategory(cat, p);

        List<PostDto> postDtos = pages.map((post) -> this.modelMapper.map(post,PostDto.class)).getContent();

        PostResponse postResponse = new PostResponse();
        postResponse.setPosts(postDtos);
        postResponse.setPageNo(pages.getNumber());
        postResponse.setPageSize(pages.getSize());
        postResponse.setTotalElements(pages.getTotalElements());
        postResponse.setTotalPages(pages.getTotalPages());
        postResponse.setLastPage(pages.isLast());

        return postResponse;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        List<Post> posts = this.postRepo.findByTitleContaining(keyword);
        List<PostDto> postDtos = posts.stream().map((p)->this.modelMapper.map(p,PostDto.class)).toList();
        return postDtos;
    }
}

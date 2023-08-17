package com.example.blog_app.demo_blogging.controller;

import com.example.blog_app.demo_blogging.config.AppConstants;
import com.example.blog_app.demo_blogging.payload.ApiResponse;
import com.example.blog_app.demo_blogging.payload.PostDto;
import com.example.blog_app.demo_blogging.payload.PostResponse;
import com.example.blog_app.demo_blogging.service.FileService;
import com.example.blog_app.demo_blogging.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/blogs/api/")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;


    @PostMapping("/user/{userid}/category/{catId}/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId,
            @PathVariable Integer catId)
    {
        PostDto newPost = this.postService.createPost(postDto,userId,catId);

        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userid}/posts")
    public ResponseEntity<PostResponse> getAllPostsByUser(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pgNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pgSize,
            @PathVariable Integer userId)
    {
     PostResponse response = this.postService.getPostsByUser(userId,pgNo,pgSize);


     return new ResponseEntity<>(response,HttpStatus.OK);

    }
    @GetMapping("/category/{catId}/posts")
    public ResponseEntity<PostResponse> getAllPostsByCategory(@PathVariable Integer catId,
            @RequestParam(value = "pgNo",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pgNo,
            @RequestParam(value = "pgSize",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pgSize
    ){
        PostResponse resp = this.postService.getPostsByCategory(catId,pgNo,pgSize);

        return new ResponseEntity<>(resp,HttpStatus.OK);

    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.PAGE_NUMBER ,required = false) Integer pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY  ,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){

        PostResponse postResponse = this.postService.getAllPosts(pageNo, pageSize,sortBy,sortDir);

        return new ResponseEntity<>(postResponse,HttpStatus.OK);

    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Integer postId){
        this.postService.deletePost(postId);
        return new ResponseEntity<>(new ApiResponse("Post Deleted Successfully!",true),HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,@PathVariable Integer postId){

        PostDto updatedPostDto = this.postService.updatePost(postDto, postId);

        return new ResponseEntity<>(updatedPostDto,HttpStatus.OK);
    }

    //Searching API
    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDto>> searchPostByTitle(
            @PathVariable("keywords") String keywords){
        List<PostDto> result = this.postService.searchPosts(keywords);
        return new ResponseEntity<>(result,HttpStatus.OK);

    }

    //post Image upload

    @PostMapping("/posts/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile file,
                                                   @PathVariable Integer postId) throws IOException {

        PostDto postDto = this.postService.getPost(postId);

        String fileName = this.fileService.uploadImage(path, file);

        postDto.setImageName(fileName);

        PostDto updatedPostDto = this.postService.updatePost(postDto, postId);

        return new ResponseEntity<>(updatedPostDto,HttpStatus.OK);
    }

    @GetMapping(value ="/posts/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName ,
            HttpServletResponse response) throws IOException {

        InputStream stream = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(stream, response.getOutputStream());


    }
}

package com.example.blog_app.demo_blogging.controller;

import com.example.blog_app.demo_blogging.payload.FileResponse;
import com.example.blog_app.demo_blogging.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/blogs/api")
public class FileController {

    @Autowired
    private FileService service;

    @Value("${project.image}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadImage(
            @RequestBody MultipartFile image
            ){

        String fileName = null;

        try{
            fileName = this.service.uploadImage(path,image);

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Image not uploaded sorry");
        }
        return new ResponseEntity<>(new FileResponse(fileName,"Image Uploaded Successfully"), HttpStatus.OK);
    }

    @GetMapping(value = "/images/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {

    InputStream resource =this.service.getResource(path,imageName);
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    StreamUtils.copy(resource,response.getOutputStream());
}
}

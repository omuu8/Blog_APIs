package com.example.blog_app.demo_blogging.service.impl;

import com.example.blog_app.demo_blogging.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        String name = file.getOriginalFilename();

        String randomId = UUID.randomUUID().toString();

        String randomFileName = randomId.concat(name.substring(name.lastIndexOf(".")));

        String newFilePath = path + File.separator+ randomFileName;

        File newFile = new File(path);

        if(!newFile.exists()) {
            newFile.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(newFilePath));

        return randomFileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path+File.separator+fileName;
        InputStream is = new FileInputStream(fullPath);
        return is;
    }
}

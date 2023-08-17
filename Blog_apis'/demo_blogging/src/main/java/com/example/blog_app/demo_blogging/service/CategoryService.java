package com.example.blog_app.demo_blogging.service;

import com.example.blog_app.demo_blogging.payload.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto,Integer categoryId);
    //delete
    void deleteCategory(Integer categoryId);
    //get
    //No need to mention it as public as by default the access modifier is public in case of interface

    CategoryDto getCategory(Integer categoryId);
    //getAll
    List<CategoryDto> getAllCategories();

}

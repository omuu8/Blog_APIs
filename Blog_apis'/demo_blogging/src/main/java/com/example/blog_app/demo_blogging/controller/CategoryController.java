package com.example.blog_app.demo_blogging.controller;


import com.example.blog_app.demo_blogging.payload.ApiResponse;
import com.example.blog_app.demo_blogging.payload.CategoryDto;
import com.example.blog_app.demo_blogging.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto
    ){
        CategoryDto createCatDto = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(createCatDto, HttpStatus.CREATED);

    }

    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Integer catId
    ){
        CategoryDto updatedCatDto = this.categoryService.updateCategory(categoryDto,catId);
        return new ResponseEntity<CategoryDto>(updatedCatDto, HttpStatus.OK);

    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId){
        this.categoryService.deleteCategory(catId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Category Deleted Successfully!",true)
                ,HttpStatus.OK);
    }
    //get
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer catId){
        CategoryDto catDto = this.categoryService.getCategory(catId);
        return new ResponseEntity<CategoryDto>(catDto,HttpStatus.OK);
    }
    //getall
    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        List<CategoryDto> categoryDtos = this.categoryService.getAllCategories();
        return new ResponseEntity<List<CategoryDto>>(categoryDtos,HttpStatus.OK);
    }


}

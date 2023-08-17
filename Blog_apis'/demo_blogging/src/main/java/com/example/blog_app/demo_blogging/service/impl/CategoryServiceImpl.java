package com.example.blog_app.demo_blogging.service.impl;

import com.example.blog_app.demo_blogging.exception.ResourceNotFoundException;
import com.example.blog_app.demo_blogging.model.Category;
import com.example.blog_app.demo_blogging.payload.CategoryDto;
import com.example.blog_app.demo_blogging.repo.CategoryRepo;
import com.example.blog_app.demo_blogging.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category cat = this.modelMapper.map(categoryDto,Category.class);
        Category addedCat = this.categoryRepo.save(cat);
        return modelMapper.map(addedCat, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category Id",categoryId));
        if(categoryDto.getCategoryTitle() != null) {
            cat.setCategoryTitle(categoryDto.getCategoryTitle());
        }
        if(categoryDto.getCategoryDescription() != null) {
            cat.setCategoryDescription(categoryDto.getCategoryDescription());
        }
        Category updatedCat = this.categoryRepo.save(cat);
        return this.modelMapper.map(updatedCat, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category Id",categoryId));
        this.categoryRepo.delete(cat);
    }

    @Override
    public CategoryDto getCategory(Integer categoryId) {
        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","Category Id",categoryId));

        return this.modelMapper.map(cat, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = this.categoryRepo.findAll();

        List<CategoryDto> categoryDtos = categories.stream()
                .map((category) -> this.modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
        return categoryDtos;
    }
}

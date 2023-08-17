package com.example.blog_app.demo_blogging.repo;

import com.example.blog_app.demo_blogging.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Integer> {

}

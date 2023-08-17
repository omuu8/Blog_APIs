package com.example.blog_app.demo_blogging.repo;

import com.example.blog_app.demo_blogging.model.Category;
import com.example.blog_app.demo_blogging.model.Post;
import com.example.blog_app.demo_blogging.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post,Integer> {

    Page<Post> findAllByUser(User user, Pageable p);

    Page<Post> findAllByCategory(Category cat, Pageable p);

    List<Post> findByTitleContaining(String title);
}

package com.example.blog_app.demo_blogging.repo;

import com.example.blog_app.demo_blogging.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment,Integer> {
}

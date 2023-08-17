package com.example.blog_app.demo_blogging.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name ="comments")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
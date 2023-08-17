package com.example.blog_app.demo_blogging.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Role {

    @Id
    private int id;

    private String name;

}
package com.example.blog_app.demo_blogging.repo;

import com.example.blog_app.demo_blogging.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Integer> {

}

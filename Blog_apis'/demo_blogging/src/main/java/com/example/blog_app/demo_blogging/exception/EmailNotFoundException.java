package com.example.blog_app.demo_blogging.exception;

import lombok.Data;

@Data
public class EmailNotFoundException extends RuntimeException{

    String email;
    String message = "emailNotFound";

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return super.getMessage();
    }

    public EmailNotFoundException(String email, String emailNotFound) {
        super();
        this.email = email;
        emailNotFound = this.message;
    }
}

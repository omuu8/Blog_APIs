package com.example.blog_app.demo_blogging.exception;

public class ApiException extends RuntimeException{

    public ApiException(String message) {
        super(message);

    }

    public ApiException() {
        super();
    }
}

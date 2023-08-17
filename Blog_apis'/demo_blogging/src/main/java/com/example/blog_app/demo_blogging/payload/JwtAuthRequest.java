package com.example.blog_app.demo_blogging.payload;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtAuthRequest {

    private String username;
    private String password;
}

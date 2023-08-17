package com.example.blog_app.demo_blogging.payload;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtAuthResponse {

    private String token;

    private UserDto userDto;
}

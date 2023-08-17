package com.example.blog_app.demo_blogging.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data

public class UserDto {

    private int id;

    @NotEmpty
    @Size(min = 4, message = "Username must be of min 4 chars !")
    private String name;

    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty
    @Size(min = 3, max = 10, message = "Password must be of min 3 chars and max 10!")
    //@Pattern can also be used in order to specify that password must have particular properties

    private String password;

    @NotEmpty
    private String about;

    private Set<RoleDto> roles = new HashSet<>();

}
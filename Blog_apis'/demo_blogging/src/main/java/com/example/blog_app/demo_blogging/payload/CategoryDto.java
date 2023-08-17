package com.example.blog_app.demo_blogging.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class CategoryDto {

    private Integer categoryId;
    @NotBlank
    @Size(min = 3, max = 20, message = "Size of the title must be between 3 to 20 !")
    private String categoryTitle;

    @NotBlank
    @Size(min = 10, max = 100000, message = "Size of the title must be between 10 to 100000 !")
    private String categoryDescription;

}
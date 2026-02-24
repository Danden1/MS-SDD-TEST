package com.example.ms_demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String content;
}

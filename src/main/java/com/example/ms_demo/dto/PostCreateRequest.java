package com.example.ms_demo.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCreateRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotBlank String author
) {
}

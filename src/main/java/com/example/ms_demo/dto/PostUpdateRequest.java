package com.example.ms_demo.dto;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateRequest(
        @NotBlank String title,
        @NotBlank String content
) {
}

package com.example.ms_demo.controller;

import com.example.ms_demo.common.ApiResponse;
import com.example.ms_demo.dto.PostCreateRequest;
import com.example.ms_demo.dto.PostResponse;
import com.example.ms_demo.dto.PostUpdateRequest;
import com.example.ms_demo.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostResponse> createPost(@Valid @RequestBody PostCreateRequest request) {
        return ApiResponse.ok("Post created", postService.createPost(request));
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts() {
        return ApiResponse.ok(postService.getPosts());
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long id) {
        return ApiResponse.ok(postService.getPost(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request) {
        return ApiResponse.ok("Post updated", postService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}

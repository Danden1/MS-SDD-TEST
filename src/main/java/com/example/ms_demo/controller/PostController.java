package com.example.ms_demo.controller;

import com.example.ms_demo.dto.PostRequest;
import com.example.ms_demo.dto.PostResponse;
import com.example.ms_demo.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostResponse> list() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public PostResponse get(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@Valid @RequestBody PostRequest request,
                               @AuthenticationPrincipal UserDetails userDetails) {
        return postService.create(request, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id,
                               @Valid @RequestBody PostRequest request,
                               @AuthenticationPrincipal UserDetails userDetails) {
        return postService.update(id, request, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal UserDetails userDetails) {
        postService.delete(id, userDetails.getUsername());
    }
}

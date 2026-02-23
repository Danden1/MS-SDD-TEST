package com.example.ms_demo.service;

import com.example.ms_demo.dto.PostRequest;
import com.example.ms_demo.dto.PostResponse;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest request);

    List<PostResponse> getAllPosts();

    PostResponse getPost(Long id);

    PostResponse updatePost(Long id, PostRequest request);

    void deletePost(Long id);
}

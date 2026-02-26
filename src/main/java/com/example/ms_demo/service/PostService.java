package com.example.ms_demo.service;

import com.example.ms_demo.dto.PostRequest;
import com.example.ms_demo.dto.PostResponse;
import com.example.ms_demo.entity.Post;
import com.example.ms_demo.exception.PostNotFoundException;
import com.example.ms_demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse create(PostRequest request) {
        Post post = new Post(request.getTitle(), request.getContent(), request.getAuthorNickname());
        return new PostResponse(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAll() {
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return new PostResponse(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        postRepository.delete(post);
    }
}

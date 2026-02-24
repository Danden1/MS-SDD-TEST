package com.example.ms_demo.service;

import com.example.ms_demo.dto.PostRequest;
import com.example.ms_demo.dto.PostResponse;
import com.example.ms_demo.entity.Post;
import com.example.ms_demo.entity.User;
import com.example.ms_demo.repository.PostRepository;
import com.example.ms_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostResponse> findAll() {
        return postRepository.findAllWithAuthor().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public PostResponse findById(Long id) {
        Post post = getPost(id);
        return new PostResponse(post);
    }

    @Transactional
    public PostResponse create(PostRequest request, String username) {
        User author = getUser(username);
        Post post = new Post(request.getTitle(), request.getContent(), author);
        return new PostResponse(postRepository.save(post));
    }

    @Transactional
    public PostResponse update(Long id, PostRequest request, String username) {
        Post post = getPost(id);
        checkOwnership(post, username);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        return new PostResponse(post);
    }

    @Transactional
    public void delete(Long id, String username) {
        Post post = getPost(id);
        checkOwnership(post, username);
        postRepository.delete(post);
    }

    private Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found: " + id));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username));
    }

    private void checkOwnership(Post post, String username) {
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to modify this post");
        }
    }
}

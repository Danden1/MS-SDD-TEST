package com.example.ms_demo.service;

import com.example.ms_demo.dto.PostRequest;
import com.example.ms_demo.dto.PostResponse;
import com.example.ms_demo.entity.Post;
import com.example.ms_demo.exception.PostNotFoundException;
import com.example.ms_demo.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;

    @BeforeEach
    void setUp() throws Exception {
        post = Post.builder().title("Test Title").content("Test Content").build();

        // Inject id via reflection since it's managed by JPA
        var idField = Post.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(post, 1L);

        var createdAtField = Post.class.getDeclaredField("createdAt");
        createdAtField.setAccessible(true);
        createdAtField.set(post, LocalDateTime.now());

        var updatedAtField = Post.class.getDeclaredField("updatedAt");
        updatedAtField.setAccessible(true);
        updatedAtField.set(post, LocalDateTime.now());
    }

    @Test
    void createPost_shouldReturnPostResponse() {
        PostRequest request = new PostRequest("Test Title", "Test Content");
        given(postRepository.save(any(Post.class))).willReturn(post);

        PostResponse response = postService.createPost(request);

        assertThat(response.title()).isEqualTo("Test Title");
        assertThat(response.content()).isEqualTo("Test Content");
        then(postRepository).should().save(any(Post.class));
    }

    @Test
    void getAllPosts_shouldReturnListOfPostResponses() {
        given(postRepository.findAll()).willReturn(List.of(post));

        List<PostResponse> responses = postService.getAllPosts();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).title()).isEqualTo("Test Title");
    }

    @Test
    void getPost_shouldReturnPostResponse_whenPostExists() {
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        PostResponse response = postService.getPost(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Test Title");
    }

    @Test
    void getPost_shouldThrowPostNotFoundException_whenPostNotFound() {
        given(postRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPost(99L))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void updatePost_shouldReturnUpdatedPostResponse() {
        PostRequest request = new PostRequest("Updated Title", "Updated Content");
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        PostResponse response = postService.updatePost(1L, request);

        assertThat(response.title()).isEqualTo("Updated Title");
        assertThat(response.content()).isEqualTo("Updated Content");
    }

    @Test
    void updatePost_shouldThrowPostNotFoundException_whenPostNotFound() {
        PostRequest request = new PostRequest("Title", "Content");
        given(postRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postService.updatePost(99L, request))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deletePost_shouldDeletePost_whenPostExists() {
        given(postRepository.existsById(1L)).willReturn(true);

        postService.deletePost(1L);

        then(postRepository).should().deleteById(1L);
    }

    @Test
    void deletePost_shouldThrowPostNotFoundException_whenPostNotFound() {
        given(postRepository.existsById(99L)).willReturn(false);

        assertThatThrownBy(() -> postService.deletePost(99L))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("99");
    }
}

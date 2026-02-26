package com.example.ms_demo.service;

import com.example.ms_demo.dto.PostCreateRequest;
import com.example.ms_demo.dto.PostResponse;
import com.example.ms_demo.dto.PostUpdateRequest;
import com.example.ms_demo.entity.Post;
import com.example.ms_demo.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post post;

    @BeforeEach
    void setUp() {
        post = new Post("Test Title", "Test Content", "author1");
    }

    @Test
    @DisplayName("게시글 생성 성공")
    void createPost_success() {
        given(postRepository.save(any(Post.class))).willReturn(post);

        PostCreateRequest request = new PostCreateRequest("Test Title", "Test Content", "author1");
        PostResponse response = postService.createPost(request);

        assertThat(response.title()).isEqualTo("Test Title");
        assertThat(response.content()).isEqualTo("Test Content");
        assertThat(response.author()).isEqualTo("author1");
    }

    @Test
    @DisplayName("게시글 목록 조회")
    void getPosts_success() {
        given(postRepository.findAll()).willReturn(List.of(post));

        List<PostResponse> responses = postService.getPosts();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).title()).isEqualTo("Test Title");
    }

    @Test
    @DisplayName("게시글 단건 조회 성공")
    void getPost_success() {
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        PostResponse response = postService.getPost(1L);

        assertThat(response.title()).isEqualTo("Test Title");
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회 시 예외 발생")
    void getPost_notFound() {
        given(postRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPost(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Post not found");
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updatePost_success() {
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        PostUpdateRequest request = new PostUpdateRequest("Updated Title", "Updated Content");
        PostResponse response = postService.updatePost(1L, request);

        assertThat(response.title()).isEqualTo("Updated Title");
        assertThat(response.content()).isEqualTo("Updated Content");
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void deletePost_success() {
        given(postRepository.existsById(1L)).willReturn(true);

        postService.deletePost(1L);

        verify(postRepository).deleteById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제 시 예외 발생")
    void deletePost_notFound() {
        given(postRepository.existsById(99L)).willReturn(false);

        assertThatThrownBy(() -> postService.deletePost(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Post not found");
    }
}

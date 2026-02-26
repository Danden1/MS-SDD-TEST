package com.example.ms_demo.controller;

import com.example.ms_demo.dto.PostCreateRequest;
import com.example.ms_demo.dto.PostResponse;
import com.example.ms_demo.dto.PostUpdateRequest;
import com.example.ms_demo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    private PostResponse sampleResponse() {
        return new PostResponse(1L, "Title", "Content", "author1",
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("POST /api/posts - 게시글 생성")
    void createPost() throws Exception {
        given(postService.createPost(any())).willReturn(sampleResponse());

        PostCreateRequest request = new PostCreateRequest("Title", "Content", "author1");

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Title"));
    }

    @Test
    @DisplayName("GET /api/posts - 게시글 목록 조회")
    void getPosts() throws Exception {
        given(postService.getPosts()).willReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Title"));
    }

    @Test
    @DisplayName("GET /api/posts/{id} - 게시글 단건 조회")
    void getPost() throws Exception {
        given(postService.getPost(1L)).willReturn(sampleResponse());

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/posts/{id} - 게시글 수정")
    void updatePost() throws Exception {
        PostResponse updated = new PostResponse(1L, "Updated", "Updated Content", "author1",
                LocalDateTime.now(), LocalDateTime.now());
        given(postService.updatePost(eq(1L), any())).willReturn(updated);

        PostUpdateRequest request = new PostUpdateRequest("Updated", "Updated Content");

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Updated"));
    }

    @Test
    @DisplayName("DELETE /api/posts/{id} - 게시글 삭제")
    void deletePost() throws Exception {
        doNothing().when(postService).deletePost(1L);

        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());
    }
}

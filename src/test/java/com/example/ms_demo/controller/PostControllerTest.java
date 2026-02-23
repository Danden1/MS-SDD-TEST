package com.example.ms_demo.controller;

import com.example.ms_demo.dto.PostRequest;
import com.example.ms_demo.dto.PostResponse;
import com.example.ms_demo.exception.PostNotFoundException;
import com.example.ms_demo.service.PostService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    private PostResponse sampleResponse() {
        return new PostResponse(1L, "Test Title", "Test Content",
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void createPost_shouldReturn201WithPostResponse() throws Exception {
        PostRequest request = new PostRequest("Test Title", "Test Content");
        given(postService.createPost(any(PostRequest.class))).willReturn(sampleResponse());

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Title"))
                .andExpect(jsonPath("$.data.content").value("Test Content"));
    }

    @Test
    void createPost_shouldReturn400_whenTitleIsBlank() throws Exception {
        PostRequest request = new PostRequest("", "Test Content");

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getAllPosts_shouldReturnListOfPosts() throws Exception {
        given(postService.getAllPosts()).willReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].title").value("Test Title"));
    }

    @Test
    void getPost_shouldReturnPost_whenExists() throws Exception {
        given(postService.getPost(1L)).willReturn(sampleResponse());

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Title"));
    }

    @Test
    void getPost_shouldReturn404_whenNotFound() throws Exception {
        given(postService.getPost(99L)).willThrow(new PostNotFoundException(99L));

        mockMvc.perform(get("/api/posts/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void updatePost_shouldReturnUpdatedPost() throws Exception {
        PostRequest request = new PostRequest("Updated Title", "Updated Content");
        PostResponse updated = new PostResponse(1L, "Updated Title", "Updated Content",
                LocalDateTime.now(), LocalDateTime.now());
        given(postService.updatePost(eq(1L), any(PostRequest.class))).willReturn(updated);

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Updated Title"));
    }

    @Test
    void updatePost_shouldReturn404_whenPostNotFound() throws Exception {
        PostRequest request = new PostRequest("Title", "Content");
        given(postService.updatePost(eq(99L), any(PostRequest.class)))
                .willThrow(new PostNotFoundException(99L));

        mockMvc.perform(put("/api/posts/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void deletePost_shouldReturn204_whenDeleted() throws Exception {
        willDoNothing().given(postService).deletePost(1L);

        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePost_shouldReturn404_whenNotFound() throws Exception {
        willThrow(new PostNotFoundException(99L)).given(postService).deletePost(99L);

        mockMvc.perform(delete("/api/posts/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}

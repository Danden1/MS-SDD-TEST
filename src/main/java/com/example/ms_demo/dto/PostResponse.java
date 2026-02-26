package com.example.ms_demo.dto;

import com.example.ms_demo.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String authorNickname;
    private final LocalDateTime createdAt;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorNickname = post.getAuthorNickname();
        this.createdAt = post.getCreatedAt();
    }
}

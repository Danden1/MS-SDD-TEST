package com.example.ms_demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String authorNickname;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Post(String title, String content, String authorNickname) {
        this.title = title;
        this.content = content;
        this.authorNickname = authorNickname;
        this.createdAt = LocalDateTime.now();
    }
}

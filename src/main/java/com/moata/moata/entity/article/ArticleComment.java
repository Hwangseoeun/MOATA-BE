package com.moata.moata.entity.article;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "article_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private long commentId;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article articleId;

    @Column(name = "content", length = 10000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, length = 10)
    private String createdBy;

    @Column(name = "comment_bool", nullable = false)
    private boolean commentBool;

    @Builder
    public ArticleComment(Article articleId, String content, LocalDateTime createdAt, String createdBy, Boolean commentBool) {
        this.articleId = articleId;
        this.content = content;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.commentBool = commentBool;
    }
}

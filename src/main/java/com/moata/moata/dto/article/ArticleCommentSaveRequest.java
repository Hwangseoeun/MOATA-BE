package com.moata.moata.dto.article;

import com.moata.moata.entity.article.Article;
import com.moata.moata.entity.article.ArticleComment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleCommentSaveRequest {
    private String content;
    private String createdBy;

    public ArticleComment toModel(Article article) {
        return ArticleComment.builder()
                .articleId(article)
                .content(content)
                .createdAt(LocalDateTime.now())
                .createdBy(createdBy)
                .isComment(true)
                .build();
    }
}

package com.moata.moata.dto.article;

import com.moata.moata.entity.article.Article;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ArticleResponse {
    private long articleId;
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private int commentCount;
    private int likeCount;
    private boolean liked;

    public static ArticleResponse from(Article article, int commentCount, int likeCount, boolean liked) {
        return ArticleResponse.builder()
                .articleId(article.getArticleId())
                .content(article.getContent())
                .createdAt(article.getCreatedAt())
                .createdBy(article.getCreatedBy())
                .commentCount(commentCount)
                .likeCount(likeCount)
                .liked(liked)
                .build();
    }
}

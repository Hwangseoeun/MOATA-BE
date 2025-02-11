package com.moata.moata.repository.article;

import com.moata.moata.entity.article.Article;
import com.moata.moata.entity.article.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    List<ArticleComment> findByArticleIdAndCommentBool(Article article, Boolean commentBool);
    Boolean existsByArticleIdAndCreatedByAndCommentBool(Article article, String userName, Boolean commentBool);
    void deleteByArticleId(Article article);
    void deleteByArticleIdAndCreatedByAndCommentBool(Article article, String createdBy, Boolean commentBool);
    int countByArticleIdAndCommentBool(Article article, Boolean commentBool);
}

package com.moata.moata.repository.article;

import com.moata.moata.entity.article.Article;
import com.moata.moata.entity.article.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    List<ArticleComment> findByArticleIdAndComment(Article article, Boolean isComment);
    Boolean existsByArticleIdAndCreatedByAndComment(Article article, String userName, Boolean isComment);
    void deleteByArticleId(Article article);
    void deleteByArticleIdAndCreatedByAndComment(Article article, String createdBy, Boolean isComment);
    int countByArticleIdAndComment(Article article, Boolean isComment);
}

package com.moata.moata.repository.article;

import com.moata.moata.entity.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAll();
    List<Article> findByContentContainingOrCreatedBy(String content, String createdBy);
    Optional<Article> findByArticleId(long id);
    Article save(Article article);
}

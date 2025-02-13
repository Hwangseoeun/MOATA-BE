package com.moata.moata.repository.article;

import com.moata.moata.entity.article.Article;
import com.moata.moata.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByGroupIdIn(List<Group> groups);
    List<Article> findByContentContainingOrCreatedByAndGroupIdIn(String content, String createdBy, List<Group> groups);
    Optional<Article> findByArticleId(long id);
}

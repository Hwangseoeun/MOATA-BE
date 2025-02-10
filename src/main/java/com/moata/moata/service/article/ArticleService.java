package com.moata.moata.service.article;

import com.moata.moata.dto.article.*;
import com.moata.moata.entity.article.Article;
import com.moata.moata.entity.article.ArticleComment;
import com.moata.moata.entity.group.Group;
import com.moata.moata.repository.article.ArticleCommentRepository;
import com.moata.moata.repository.article.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public List<ArticleResponse> findAll(long userId) {
        List<Article> articles = articleRepository.findAll();
        return entityToResponse(articles, userId);
    }

    public List<ArticleResponse> findByKeywordOrUserId(String keyword, long userId, String userName) {
        List<Article> articles = articleRepository.findByContentContainingOrCreatedBy(keyword, userName);
        return entityToResponse(articles, userId);
    }

    private List<ArticleResponse> entityToResponse(List<Article> articles, long userId) {
        //유저 구현 시 사용
        String userName = userRepository.findByUserId(userId)
                                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        return articles.stream().map(article -> {
            int commentCount = articleCommentRepository.countByArticleIdAndComment(article, true);
            int likeCount = articleCommentRepository.countByArticleIdAndComment(article, false);
            boolean liked = articleCommentRepository.existsByArticleIdAndCreatedByAndComment(article, userName, false);
            return ArticleResponse.from(article, commentCount, likeCount, liked);
        }).toList();
    }

    public ArticleWithCommentResponse findByIdWithComment(Long id) {
        Article article = articleRepository.findByArticleId(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글"));
        List<ArticleCommentResponse> comments = articleCommentRepository.findByArticleIdAndComment(article, true).stream().map(ArticleCommentResponse::from).toList();
        return ArticleWithCommentResponse.from(article, comments);
    }

    public void saveArticle(ArticleSaveRequest articleSaveRequest) {
        Group group = groupRepository.findByGroupId(articleSaveRequest.getGroupId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 그룹"));
        articleRepository.save(articleSaveRequest.toModel(group));
    }

    public void saveComment(long articleId, ArticleCommentSaveRequest articleCommentSaveRequest) {
        Article article = articleRepository.findByArticleId(articleId).orElseThrow(NoSuchElementException::new);
        ArticleComment comment = articleCommentSaveRequest.toModel(article);
        articleCommentRepository.save(comment);
    }

    public void saveLike(long articleId, long userId) {
        Article article = articleRepository.findByArticleId(articleId).orElseThrow(NoSuchElementException::new);
        String userName = userRepository.findByUserId(userId)
                                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        ArticleComment like = ArticleComment.builder()
                .articleId(article)
                .createdAt(LocalDateTime.now())
                .createdBy("user")
                .isComment(false)
                .build();

        articleCommentRepository.save(like);
    }

    @Transactional
    public void deleteArticle(long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글"));
        articleCommentRepository.deleteByArticleId(article);
        articleRepository.delete(article);
    }

    public void deleteComment(long commentId) {
        articleCommentRepository.deleteById(commentId);
    }

    public void deleteLike(long userId, long articleId) {
        String userName = userRepository.findByUserId(userId)
                                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        Article article = articleRepository.findById(articleId).orElseThrow(NoSuchElementException::new);
        articleCommentRepository.deleteByArticleIdAndCreatedByAndComment(article, "placeholder",false);
    }
}
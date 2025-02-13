package com.moata.moata.service.article;

import com.moata.moata.dto.article.*;
import com.moata.moata.dto.group.GroupRuleResponse;
import com.moata.moata.dto.group.GroupRuleSaveRequest;
import com.moata.moata.entity.article.Article;
import com.moata.moata.entity.article.ArticleComment;
import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.group.GroupRule;
import com.moata.moata.entity.group.MatchingGroup;
import com.moata.moata.entity.user.User;
import com.moata.moata.repository.article.ArticleCommentRepository;
import com.moata.moata.repository.article.ArticleRepository;
import com.moata.moata.repository.group.GroupRepository;
import com.moata.moata.repository.group.GroupRuleRepository;
import com.moata.moata.repository.group.MatchingGroupRepository;
import com.moata.moata.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final GroupRepository groupRepository;
    private final GroupRuleRepository groupRuleRepository;
    private final UserRepository userRepository;
    private final MatchingGroupRepository matchingGroupRepository;

    public List<Group> findMatchingGroup(long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        return matchingGroupRepository.findByParticipantId(user).stream().map(MatchingGroup::getGroupId).collect(Collectors.toList());
    }

    public List<ArticleResponse> findAll(long userId) {
        List<Article> articles = articleRepository.findByGroupIdIn(findMatchingGroup(userId));
        return entityToResponse(articles, userId);
    }

    public List<ArticleResponse> findByKeywordOrUserId(String keyword, long userId, String userName) {
        List<Article> articles = articleRepository.findByContentContainingOrCreatedByAndGroupIdIn(keyword, userName, findMatchingGroup(userId));
        return entityToResponse(articles, userId);
    }

    private List<ArticleResponse> entityToResponse(List<Article> articles, long userId) {
        //유저 구현 시 사용
        String userName = userRepository.findUserNameByUserId(userId)
                                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        return articles.stream().map(article -> {
            int commentCount = articleCommentRepository.countByArticleIdAndCommentBool(article, true);
            int likeCount = articleCommentRepository.countByArticleIdAndCommentBool(article, false);
            boolean liked = articleCommentRepository.existsByArticleIdAndCreatedByAndCommentBool(article, userName, false);
            return ArticleResponse.from(article, commentCount, likeCount, liked);
        }).toList();
    }

    public ArticleWithCommentResponse findByIdWithComment(Long id) {
        Article article = articleRepository.findByArticleId(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글"));
        List<ArticleCommentResponse> comments = articleCommentRepository.findByArticleIdAndCommentBool(article, true).stream().map(ArticleCommentResponse::from).toList();
        return ArticleWithCommentResponse.from(article, comments);
    }

    public GroupRuleResponse findGroupRule(long userId) {
        List<GroupRule> groupRules = groupRuleRepository.findByGroupIdIn(findMatchingGroup(userId));
        return GroupRuleResponse.from(groupRules.get(0));
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
        String userName = userRepository.findUserNameByUserId(userId)
                                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        ArticleComment like = ArticleComment.builder()
                .articleId(article)
                .createdAt(LocalDateTime.now())
                .createdBy(userName)
                .commentBool(false)
                .build();

        articleCommentRepository.save(like);
    }

    public void saveGroupRule(long userId, GroupRuleSaveRequest groupRuleSaveRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자"));
        Group group = groupRepository.findByOwnerId(user)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 그룹"));
        groupRuleRepository.save(groupRuleSaveRequest.toModel(group));
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
        String userName = userRepository.findUserNameByUserId(userId)
                                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        Article article = articleRepository.findById(articleId).orElseThrow(NoSuchElementException::new);
        articleCommentRepository.deleteByArticleIdAndCreatedByAndCommentBool(article, userName,false);
    }
}
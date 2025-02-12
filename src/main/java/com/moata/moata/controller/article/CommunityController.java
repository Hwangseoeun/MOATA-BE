package com.moata.moata.controller.article;

import com.moata.moata.config.jwt.TokenProvider;
import com.moata.moata.dto.article.ArticleCommentSaveRequest;
import com.moata.moata.dto.article.ArticleResponse;
import com.moata.moata.dto.article.ArticleSaveRequest;
import com.moata.moata.dto.article.ArticleWithCommentResponse;
import com.moata.moata.dto.group.GroupRuleResponse;
import com.moata.moata.dto.group.GroupRuleSaveRequest;
import com.moata.moata.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/community")
public class CommunityController {

    private final ArticleService articleService;
    private final TokenProvider tokenProvider;

    @GetMapping("/all")
    public ResponseEntity<List<ArticleResponse>> getArticleAll(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = tokenProvider.getUserId(token);

        return ResponseEntity.ok().body(articleService.findAll(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleResponse>> searchArticle(@RequestHeader("Authorization") String authorizationHeader,
                                                               @RequestParam(name = "keyword") String keyword,
                                                               @RequestParam(name = "userName") String userName) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = tokenProvider.getUserId(token);

        return ResponseEntity.ok().body(articleService.findByKeywordOrUserId(keyword, userId, userName));
    }

    @GetMapping("{article_id}")
    public ResponseEntity<ArticleWithCommentResponse> getArticleWithComment(@PathVariable("article_id") long articleId) {
        return ResponseEntity.ok().body(articleService.findByIdWithComment(articleId));
    }

    @GetMapping("rule")
    public ResponseEntity<GroupRuleResponse> getGroupRule() {
        return ResponseEntity.ok().body(articleService.findGroupRule());
    }

    @PostMapping
    public ResponseEntity<String> saveArticle(@RequestBody ArticleSaveRequest articleSaveRequest) {
        articleService.saveArticle(articleSaveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("rule")
    public ResponseEntity<String> saveGroupRule(@AuthenticationPrincipal long userId,
                                                @RequestBody GroupRuleSaveRequest groupRuleSaveRequest) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = tokenProvider.getUserId(token);

        articleService.saveGroupRule(userId, groupRuleSaveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("{article_id}")
    public ResponseEntity<String> saveComment(@PathVariable("article_id") long articleId, @RequestBody ArticleCommentSaveRequest articleCommentSaveRequest ) {
        articleService.saveComment(articleId, articleCommentSaveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("{article_id}/like")
    public ResponseEntity<String> saveLike(@PathVariable("article_id") long articleId, @AuthenticationPrincipal Long userId) {
        articleService.saveLike(articleId, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("{article_id}")
    public ResponseEntity<String> deleteArticle(@PathVariable("article_id") long articleId) {
        articleService.deleteArticle(articleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("comment/{comment_id}")
    public ResponseEntity<String> deleteComment(@PathVariable("comment_id") long commentId) {
        articleService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{article_id}/like")
    public ResponseEntity<String> deleteLike(@RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable("article_id") long articleId) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = tokenProvider.getUserId(token);

        articleService.deleteLike(articleId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

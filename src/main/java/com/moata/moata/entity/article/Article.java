package com.moata.moata.entity.article;

import com.moata.moata.entity.group.Group;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", nullable = false)
    private long articleId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Group groupId;

    @Column(name = "content", length = 10000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, length = 10)
    private String createdBy;

    @Builder
    public Article(Group groupId, String content, LocalDateTime createdAt, String createdBy) {
        this.groupId = groupId;
        this.content = content;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}

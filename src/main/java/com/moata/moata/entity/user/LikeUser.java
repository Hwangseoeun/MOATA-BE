package com.moata.moata.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "like_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LikeUser {

    @EmbeddedId
    private LikeUserId id;

    @ManyToOne
    @MapsId("likerId")  // LikeUserId의 likerId 필드와 매핑
    @JoinColumn(name = "liker_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private User liker;

    @ManyToOne
    @MapsId("targetId")  // LikeUserId의 targetId 필드와 매핑
    @JoinColumn(name = "target_id", nullable = false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private User target;

    @Builder
    public LikeUser(User liker, User target) {
        this.id = new LikeUserId(liker.getUserId(), target.getUserId());
        this.liker = liker;
        this.target = target;
    }
}

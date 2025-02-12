package com.moata.moata.entity.group;

import com.moata.moata.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "matching_group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MatchingGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Group groupId;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private User participantId;

    @Builder
    public MatchingGroup(Group groupId, User participantId) {
        this.groupId = groupId;
        this.participantId = participantId;
    }
}

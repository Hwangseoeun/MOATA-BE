package com.moata.moata.repository.group;

import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.group.MatchingGroup;
import com.moata.moata.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingGroupRepository extends JpaRepository<MatchingGroup, Long> {
    Optional<MatchingGroup> findByGroupId(Group groupId);

    int countByParticipantId(User participantId);

}

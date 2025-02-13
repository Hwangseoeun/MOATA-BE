package com.moata.moata.repository.group;

import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.group.MatchingGroup;
import com.moata.moata.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchingGroupRepository extends JpaRepository<MatchingGroup, Long> {
    List<MatchingGroup> findByGroupId(Group groupId);
    List<MatchingGroup> findByParticipantId(User userId);
    int countByParticipantId(User participantId);

}

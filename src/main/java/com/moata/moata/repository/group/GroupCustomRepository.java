package com.moata.moata.repository.group;

import com.moata.moata.dto.group.GroupSearchCondition;
import com.moata.moata.entity.group.Group;

import java.util.List;

public interface GroupCustomRepository {
    List<Group> searchGroups(GroupSearchCondition condition);
    void incrementMatchedCount(Long groupId);
}

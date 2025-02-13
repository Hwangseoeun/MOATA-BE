package com.moata.moata.repository.group;

import com.moata.moata.dto.group.GroupSearchByUserCondition;
import com.moata.moata.entity.group.Group;

import java.util.List;

public interface GroupCustomUserRepository {
    List<Group> searchGroupsByUser(GroupSearchByUserCondition condition);
}

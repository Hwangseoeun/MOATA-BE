package com.moata.moata.repository.group;

import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.group.GroupRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRuleRepository extends JpaRepository<GroupRule, Long> {
    List<GroupRule> findByGroupIdIn(List<Group> groups);
}

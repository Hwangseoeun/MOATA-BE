package com.moata.moata.repository.group;

import com.moata.moata.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, GroupCustomRepository {
    Optional<Group> findByGroupId(Long groupId);
}

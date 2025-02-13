package com.moata.moata.repository.group;

import com.moata.moata.dto.group.GroupSearchByUserCondition;
import com.moata.moata.entity.group.Group;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.moata.moata.entity.group.QGroup.group;
import static com.moata.moata.entity.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class GroupCustomUserRepositoryImpl implements GroupCustomUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Group> searchGroupsByUser(GroupSearchByUserCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();

        if (condition.getName() != null) {
            builder.and(group.ownerId.name.containsIgnoreCase(condition.getName()));
        }
        if (condition.getLocation() != null) {
            builder.and(group.ownerId.location.containsIgnoreCase(condition.getLocation()));
        }
        if (condition.getCarModelName() != null) {
            builder.and(group.carModelName.containsIgnoreCase(condition.getCarModelName()));
        }

        return queryFactory
                .selectFrom(group)
                .join(group.ownerId, user)
                .where(builder)
                .fetch();
    }
}
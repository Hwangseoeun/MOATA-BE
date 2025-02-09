package com.moata.moata.repository.group;

import com.moata.moata.dto.group.GroupSearchCondition;
import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.group.QGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.moata.moata.entity.group.QGroup.group;

@Repository
@RequiredArgsConstructor
public class GroupCustomRepositoryImpl implements GroupCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Group> searchGroups(GroupSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();

        if (condition.getHasCar() != null) {
            builder.and(group.hasCar.eq(condition.getHasCar()));
        }
        if (condition.getFavoriteArea() != null) {
            builder.and(group.favoriteArea.eq(condition.getFavoriteArea()));
        }
        if (condition.getCoOwnerMax() != null && condition.getCoOwnerMax() != 6) {
            builder.and(group.coOwnerMax.eq(condition.getCoOwnerMax()));
        }
        if (condition.getCoOwnerMax() != null && condition.getCoOwnerMax() == 6) {
            builder.and(group.coOwnerMax.goe(condition.getCoOwnerMax()));
        }
        if (condition.getCarType() != null) {
            builder.and(group.carType.eq(condition.getCarType()));
        }

        return queryFactory
                .selectFrom(group)
                .where(builder)
                .fetch();
    }

    @Override
    public void incrementMatchedCount(Long groupId) {
        QGroup group = QGroup.group;

        queryFactory.update(group)
                .set(group.matchedCount, group.matchedCount.add(1))  // matched_count + 1 증가
                .where(group.groupId.eq(groupId))
                .execute();
    }
}

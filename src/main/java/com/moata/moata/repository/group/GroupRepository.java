import com.moata.moata.entity.group.Group;
package com.moata.moata.repository.group;

import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, GroupCustomRepository {
    Optional<Group> findByGroupId(Long groupId);
    Optional<Group> findByOwnerId(User ownerId);

    @Query("SELECT g FROM Group g " +
            "WHERE g.hasCar <> :hasCar " +
            "ORDER BY (CASE WHEN :carType = g.carType THEN 7 ELSE 0 END + " +
            "CASE WHEN :carModelName = g.carModelName THEN 3 ELSE 0 END + " +
            "CASE WHEN ABS(:coOwnerMax - g.coOwnerMax) <= 1 THEN 5 ELSE 0 END + " +
            "CASE WHEN ABS(:carUseFrequency - g.carUseFrequency) <= 1 THEN 5 ELSE 0 END) DESC")
    List<Group> findBestMatches(
            @Param("hasCar") boolean hasCar,
            @Param("carType") String carType,
            @Param("carModelName") String carModelName,
            @Param("coOwnerMax") int coOwnerMax,
            @Param("carUseFrequency") int carUseFrequency
    );
}

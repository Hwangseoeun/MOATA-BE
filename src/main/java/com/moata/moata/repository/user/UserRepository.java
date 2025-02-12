package com.moata.moata.repository.user;

import com.moata.moata.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<String> findUserNameByUserId(long id);
    Optional<User> findByPhone(String phone);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.sharedCarCnt = u.sharedCarCnt + 1 WHERE u.userId = :userId")
    void incrementSharedCarCnt(@Param("userId") Long userId);
}

package com.moata.moata.repository.user;

import com.moata.moata.entity.user.LikeUser;
import com.moata.moata.entity.user.LikeUserId;
import com.moata.moata.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeUserRepository extends JpaRepository<LikeUser, LikeUserId> {
    boolean existsByLikerAndTarget(User liker, User target);
    List<LikeUser> findAllByLiker(User liker);
}

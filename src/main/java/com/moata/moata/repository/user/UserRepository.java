package com.moata.moata.repository.user;

import com.moata.moata.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<String> findUserNameByUserId(long id);
    Optional<User> findByPhone(String phone);

}

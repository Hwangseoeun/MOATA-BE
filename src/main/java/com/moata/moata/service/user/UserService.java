package com.moata.moata.service.user;

import com.moata.moata.dto.group.GroupInfoResponse;
import com.moata.moata.dto.user.UserLocationSaveRequest;
import com.moata.moata.dto.user.UserLocationUpdateRequest;
import com.moata.moata.dto.user.UserNameUpdateRequest;
import com.moata.moata.dto.user.UserProfileResponse;
import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.user.LikeUser;
import com.moata.moata.entity.user.User;
import com.moata.moata.repository.group.GroupRepository;
import com.moata.moata.repository.user.LikeUserRepository;
import com.moata.moata.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final LikeUserRepository likeUserRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public UserProfileResponse findUserProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserProfileResponse.from(user);
    }

    @Transactional
    public void saveUserLocation(Long userId, UserLocationSaveRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.saveUserLocation(request);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserName(Long userId, UserNameUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.updateUserName(request);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserLocation(Long userId, UserLocationUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.updateUserLocation(request);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
        userRepository.flush();
    }

    public void saveLike(Long likerId, Long targetId) {
        User liker = userRepository.findById(likerId)
                .orElseThrow(() -> new EntityNotFoundException("Liker user not found"));

        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new EntityNotFoundException("Target user not found"));

        if (likeUserRepository.existsByLikerAndTarget(liker, target)) {
            throw new IllegalStateException("Already liked this user.");
        }

        LikeUser likeUser = new LikeUser(liker, target);
        likeUserRepository.save(likeUser);
    }

    public void deleteLike(Long likerId, Long targetId) {
        User liker = userRepository.findById(likerId)
                .orElseThrow(() -> new EntityNotFoundException("Liker user not found"));

        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new EntityNotFoundException("Target user not found"));

        if (!likeUserRepository.existsByLikerAndTarget(liker, target)) {
            throw new IllegalStateException("User has never liked this user");
        }

        LikeUser likeUser = new LikeUser(liker, target);
        likeUserRepository.delete(likeUser);
    }

    public List<GroupInfoResponse> getLikedUsers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Group group = groupRepository.findByOwnerId(user)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        List<LikeUser> likedUsers = likeUserRepository.findAllByLiker(user);

        return likedUsers.stream()
                .map(like -> GroupInfoResponse.from(group))
                .toList();
    }
}

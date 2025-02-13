package com.moata.moata.service.user;

import com.moata.moata.config.jwt.TokenProvider;
import com.moata.moata.dto.group.GroupInfoResponse;
import com.moata.moata.dto.user.*;
import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.user.LikeUser;
import com.moata.moata.entity.user.User;
import com.moata.moata.repository.group.GroupRepository;
import com.moata.moata.repository.group.MatchingGroupRepository;
import com.moata.moata.repository.user.LikeUserRepository;
import com.moata.moata.repository.user.UserRepository;

import com.moata.moata.service.group.GroupService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.moata.moata.common.DistanceCalculator.calculateDistance;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final LikeUserRepository likeUserRepository;
    private final MatchingGroupRepository matchingGroupRepository;
    private final TokenProvider tokenProvider;

    public AuthTokens signUp(UserSignUpRequest userSignUpRequest) {

        Optional<User> existingUser = userRepository.findByPhone(userSignUpRequest.getPhone());

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = userSignUpRequest.toModel();

            userRepository.save(user);
        }

        // Access Token (1시간 유효)
        String accessToken = tokenProvider.makeToken(new Date(System.currentTimeMillis() + 36000* 1000L), user);
        // Refresh Token (7일 유효)
        String refreshToken = tokenProvider.makeToken(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L), user);

        return AuthTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public UserProfileResponse findUserProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        int sharedCarCnt = matchingGroupRepository.countByParticipantId(user);
        return UserProfileResponse.from(user, sharedCarCnt);
    }

    @Transactional
    public void updateUserName(Long userId, UserNameUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.updateUserName(request);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserLocation(Long userId, UserLocationRequest request) {
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

    @Transactional(readOnly = true)
    public List<GroupInfoResponse> getLikedUsers(Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<LikeUser> likedUsers = likeUserRepository.findAllByLiker(currentUser);

        return likedUsers.stream()
                .map(like -> {
                    User likedUser = like.getTarget();
                    Group likedUserGroup = groupRepository.findByOwnerId(likedUser)
                            .orElseThrow(() -> new EntityNotFoundException("Group not found for liked user: " + likedUser.getUserId()));

                    double distance = calculateDistance(currentUser.getLatitude(), currentUser.getLongitude(), likedUser.getLatitude(), likedUser.getLongitude());

                    return GroupInfoResponse.from(likedUserGroup, distance);
                })
                .toList();
    }

    @Transactional
    public int sharedCarCnt(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return matchingGroupRepository.countByParticipantId(user);
    }
}

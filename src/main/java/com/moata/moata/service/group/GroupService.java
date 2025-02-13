package com.moata.moata.service.group;

import com.moata.moata.dto.group.*;
import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.group.MatchingGroup;
import com.moata.moata.entity.user.User;
import com.moata.moata.repository.group.GroupCustomUserRepository;
import com.moata.moata.repository.group.GroupRepository;
import com.moata.moata.repository.group.MatchingGroupRepository;
import com.moata.moata.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.moata.moata.common.DistanceCalculator.calculateDistance;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MatchingGroupRepository matchingGroupRepository;
    private final GroupCustomUserRepository groupCustomUserRepository;

    @Transactional
    public Group saveGroup(Long userId, GroupSaveRequest request) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with id %d is not found", userId)));

        return groupRepository.save(request.toModel(user));
    }

    @Transactional(readOnly = true)
    public List<GroupInfoResponse> findAllGroups(Long userId) {

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return groupRepository.findAll().stream()
                .map(group -> {
                    User owner = group.getOwnerId();

                    double distance = calculateDistance(currentUser.getLatitude(), currentUser.getLongitude(), owner.getLatitude(), owner.getLongitude());

                    return GroupInfoResponse.from(group, distance);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GroupInfoResponse> searchGroups(GroupSearchCondition condition, Long userId) {

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return groupRepository.searchGroups(condition).stream()
                .map(group -> {
                    User owner = group.getOwnerId();

                    double distance = calculateDistance(currentUser.getLatitude(), currentUser.getLongitude(), owner.getLatitude(), owner.getLongitude());

                    return GroupInfoResponse.from(group, distance);
                })
                .toList();
    }

    public List<Group> searchGroupsByUser(GroupSearchByUserCondition condition) {
        return groupCustomUserRepository.searchGroupsByUser(condition);
    }

    @Transactional(readOnly = true)
    public GroupDetailInfoResponse findGroupByGroupId(Long groupId, Long currentUserId) {
        Group group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        User owner = group.getOwnerId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        double distance = calculateDistance(currentUser.getLatitude(), currentUser.getLongitude(), owner.getLatitude(), owner.getLongitude());

        return GroupDetailInfoResponse.from(group, distance);
    }

    public List<GroupDetailInfoResponse> getMatchingUsers(Long userId) {
        final User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id is not found", userId)));

        Group userGroup = groupRepository.findByOwnerId(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Group not found for User ID: " + userId));

        List<Group> matchedGroups = groupRepository.findBestMatches(
                userGroup.getHasCar(),
                userGroup.getCarType(),
                userGroup.getCarModelName(),
                userGroup.getCoOwnerMax(),
                userGroup.getCarUseFrequency()
        );

        return matchedGroups.stream()
                .map(group -> {
                    User owner = group.getOwnerId();
                    double distance = calculateDistance(currentUser.getLatitude(), currentUser.getLongitude(), owner.getLatitude(), owner.getLongitude());
                    return GroupDetailInfoResponse.from(group, distance);
                })
                .toList();
    }

    @Transactional
    public void increaseMatchedCount(Long groupId) {
        groupRepository.incrementMatchedCount(groupId);
    }

    @Transactional
    public void addParticipantToMatchingGroup(Long groupId, Long userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found for Group ID: " + groupId));

        User participant = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found for User ID: " + userId));

        MatchingGroup matchingGroup = MatchingGroup.builder()
                .groupId(group)
                .participantId(participant)
                .build();

        matchingGroupRepository.save(matchingGroup);
    }
}

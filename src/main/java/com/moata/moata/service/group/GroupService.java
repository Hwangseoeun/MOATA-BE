package com.moata.moata.service.group;

import com.moata.moata.dto.group.GroupDetailInfoResponse;
import com.moata.moata.dto.group.GroupInfoResponse;
import com.moata.moata.dto.group.GroupSaveRequest;
import com.moata.moata.dto.group.GroupSearchCondition;
import com.moata.moata.entity.group.Group;
import com.moata.moata.entity.user.User;
import com.moata.moata.repository.group.GroupRepository;
import com.moata.moata.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public Group saveGroup(GroupSaveRequest request) {
        final User user = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id is not found", request.getOwnerId())));
        return groupRepository.save(request.toModel(user));
    }

    public List<GroupInfoResponse> findAllGroups() {
        return groupRepository.findAll().stream()
                .map(GroupInfoResponse::from)
                .toList();
    }

    public List<GroupInfoResponse> searchGroups(GroupSearchCondition condition) {
        return groupRepository.searchGroups(condition).stream()
                .map(GroupInfoResponse::from)
                .toList();
    }

    public GroupDetailInfoResponse findGroupByGroupId(Long groupId) {
        Group group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));
        return GroupDetailInfoResponse.from(group);
    }

    public List<GroupDetailInfoResponse> getMatchingUsers(Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id is not found", userId)));

        Group group = groupRepository.findByOwnerId(user)
                .orElseThrow(() -> new EntityNotFoundException("Group not found for User ID: " + userId));

        List<Group> matchedGroups = groupRepository.findBestMatches(
                group.getHasCar(),
                group.getCarType(),
                group.getCarModelName(),
                group.getCoOwnerMax(),
                group.getCarUseFrequency()
        );

        return matchedGroups.stream()
                .map(GroupDetailInfoResponse::from)
                .toList();
    }
}

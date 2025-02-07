package com.moata.moata.service.group;

import com.moata.moata.dto.group.GroupInfoResponse;
import com.moata.moata.dto.group.GroupSaveRequest;
import com.moata.moata.entity.group.Group;
import com.moata.moata.repository.group.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public Group saveGroup(GroupSaveRequest request) {
        return groupRepository.save(request.toModel());
    }

    public List<GroupInfoResponse> findAllGroups() {
        return groupRepository.findAll().stream()
                .map(GroupInfoResponse::from)
                .collect(Collectors.toList());
    }
}

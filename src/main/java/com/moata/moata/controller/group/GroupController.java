package com.moata.moata.controller.group;

import com.moata.moata.dto.group.*;
import com.moata.moata.entity.group.Group;
import com.moata.moata.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/group")
    public ResponseEntity<GroupSaveResponse> saveGroup(GroupSaveRequest request) {
        try {
            Group group = groupService.saveGroup(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(GroupSaveResponse.builder()
                            .isSuccess(true)
                            .code(201)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(GroupSaveResponse.builder()
                            .isSuccess(false)
                            .code(400)
                            .build());
        }
    }

    @GetMapping("/group/all")
    public ResponseEntity<List<GroupInfoResponse>> findAllGroups() {
        List<GroupInfoResponse> groups = groupService.findAllGroups();
        return ResponseEntity.ok().body(groups);
    }

    @GetMapping("/group/search")
    public ResponseEntity<List<GroupInfoResponse>> searchGroups(GroupSearchCondition condition) {
        return ResponseEntity.ok().body(groupService.searchGroups(condition));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<GroupDetailInfoResponse> detailGroupInfo(@PathVariable("groupId") Long groupId) {
        GroupDetailInfoResponse response = groupService.findGroupByGroupId(groupId);
        return ResponseEntity.ok().body(response);
    }
}

package com.moata.moata.controller.group;

import com.moata.moata.config.jwt.TokenProvider;
import com.moata.moata.dto.group.*;
import com.moata.moata.entity.group.Group;
import com.moata.moata.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;
    private final TokenProvider tokenProvider;

    @PostMapping("/group")
    public ResponseEntity<GroupSaveResponse> saveGroup(@RequestHeader("Authorization") String authorizationHeader, @RequestBody GroupSaveRequest request) {
//        log.info("group save request: {}", request);
        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        try {
            Group group = groupService.saveGroup(userId, request);
            groupService.addParticipantToMatchingGroup(group.getGroupId(), userId);
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
    public ResponseEntity<List<GroupInfoResponse>> findAllGroups(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        List<GroupInfoResponse> groups = groupService.findAllGroups(userId);
        return ResponseEntity.ok().body(groups);
    }

    @GetMapping("/group/search")
    public ResponseEntity<List<GroupInfoResponse>> searchGroups(@RequestHeader("Authorization") String authorizationHeader, GroupSearchCondition condition) {

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        return ResponseEntity.ok().body(groupService.searchGroups(condition, userId));
    }

    @GetMapping("/group/search/keyword")
    public ResponseEntity<List<Group>> searchGroupsByUser(@ModelAttribute GroupSearchByUserCondition condition) {
        List<Group> groups = groupService.searchGroupsByUser(condition);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<GroupDetailInfoResponse> detailGroupInfo(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("groupId") Long groupId) {

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        GroupDetailInfoResponse response = groupService.findGroupByGroupId(groupId, userId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/group/recommendations")
    public ResponseEntity<List<GroupDetailInfoResponse>> findMatchingGroups(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        List<GroupDetailInfoResponse> groups = groupService.getMatchingUsers(userId);
        return ResponseEntity.ok().body(groups);
    }

    @PutMapping("/group/matching/{groupId}")
    public ResponseEntity<GroupMatchingResponse> matchingFinished(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long groupId) {

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        try {
            groupService.addParticipantToMatchingGroup(groupId, userId);
            groupService.increaseMatchedCount(groupId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(GroupMatchingResponse.builder()
                            .isSuccess(true)
                            .code(200)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(GroupMatchingResponse.builder()
                            .isSuccess(false)
                            .code(400)
                            .build());
        }
    }
}

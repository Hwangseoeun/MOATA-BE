package com.moata.moata.controller.group;

import com.moata.moata.config.jwt.TokenProvider;
import com.moata.moata.dto.group.*;
import com.moata.moata.entity.group.Group;
import com.moata.moata.service.group.GroupService;
import com.moata.moata.service.user.UserService;
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
    private final UserService userService;

    @PostMapping("/group")
    public ResponseEntity<GroupSaveResponse> saveGroup(@RequestBody GroupSaveRequest request) {
//        log.info("group save request: {}", request);
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

    //추후 쿼리 파라미터를 통한 userId 값을 받는 것이 아닌 jwt를 통해 조회하는 방식으로 수정 예정
    @GetMapping("/group/recommendations/{userId}")
    public ResponseEntity<List<GroupDetailInfoResponse>> findMatchingGroups(@PathVariable Long userId) {
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
            userService.increaseSharedCarCnt(userId);

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

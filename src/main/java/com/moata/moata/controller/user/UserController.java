package com.moata.moata.controller.user;

import com.moata.moata.config.jwt.TokenProvider;
import com.moata.moata.dto.group.GroupInfoResponse;
import com.moata.moata.dto.user.*;
import com.moata.moata.service.user.KakaoService;
import com.moata.moata.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final KakaoService kakaoService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @ResponseBody
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<AuthTokens> kakaoLogin(@RequestParam String code){
        try{
            return ResponseEntity.ok(kakaoService.kakaoLogin(code));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item Not Found");
        }
    }

    @GetMapping("/user/my")
    public ResponseEntity<UserProfileResponse> findUserProfile(@RequestHeader("Authorization") String authorizationHeader){

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        UserProfileResponse response = userService.findUserProfileByUserId(userId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/user/my/location")
    public ResponseEntity<HttpStatus> saveUserLocation(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserLocationRequest request){

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        userService.updateUserLocation(userId, request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/user/my/name")
    public ResponseEntity<HttpStatus> updateUserName(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserNameUpdateRequest request){

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        userService.updateUserName(userId, request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/user/my/location")
    public ResponseEntity<HttpStatus> updateUserLocation(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserLocationRequest request){

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        userService.updateUserLocation(userId, request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping("/user/my")
    public ResponseEntity<HttpStatus> deleteUserById(@RequestHeader("Authorization") String authorizationHeader){

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        userService.deleteUser(userId);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/user/like/{groupId}")
    public ResponseEntity<HttpStatus> saveLike(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("groupId") Long targetId){

        String token = authorizationHeader.replace("Bearer ", "");

        Long likerId = tokenProvider.getUserId(token);

        userService.saveLike(likerId, targetId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/user/like/{groupId}")
    public ResponseEntity<HttpStatus> deleteLike(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("groupId") Long targetId){

        String token = authorizationHeader.replace("Bearer ", "");

        Long likerId = tokenProvider.getUserId(token);

        userService.deleteLike(likerId, targetId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/user/like")
    public ResponseEntity<List<GroupInfoResponse>> getLikedUsers(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = tokenProvider.getUserId(token);

        List<GroupInfoResponse> likedUsers = userService.getLikedUsers(userId);

        return ResponseEntity.ok().body(likedUsers);
    }
}

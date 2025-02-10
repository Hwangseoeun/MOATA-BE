package com.moata.moata.controller.user;

import com.moata.moata.dto.user.AuthTokens;
import com.moata.moata.service.user.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final KakaoService kakaoService;

    @ResponseBody
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<AuthTokens> kakaoLogin(@RequestParam String code){
        try{
            return ResponseEntity.ok(kakaoService.kakaoLogin(code));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item Not Found");
        }
    }
}

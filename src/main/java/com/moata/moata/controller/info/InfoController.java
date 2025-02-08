package com.moata.moata.controller.info;

import com.moata.moata.dto.info.InfoResponse;
import com.moata.moata.service.info.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class InfoController {

    private final InfoService infoService;

    @GetMapping("/info/{type}")
    public ResponseEntity<List<InfoResponse>> findInfo(@PathVariable("type") String type) {
        List<InfoResponse> infos = infoService.findInfoByType(type);
        return ResponseEntity.ok().body(infos);
    }
}

package com.moata.moata.service.info;

import com.moata.moata.constant.InfoType;
import com.moata.moata.dto.info.InfoResponse;
import com.moata.moata.repository.info.InfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepository infoRepository;

    public List<InfoResponse> findInfoByType(String type) {
        return infoRepository.findByType(InfoType.valueOf(type.toUpperCase())).stream()
                .map(InfoResponse::from)
                .toList();
    }
}

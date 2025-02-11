package com.moata.moata.repository.info;

import com.moata.moata.constant.InfoType;
import com.moata.moata.entity.info.Info;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfoRepository extends JpaRepository<Info, Long> {
    List<Info> findByType(InfoType type);
}

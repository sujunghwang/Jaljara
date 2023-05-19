package com.ssafy.a802.jaljara.db.repository;

import com.ssafy.a802.jaljara.db.entity.ParentCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ParentCodeRepository extends CrudRepository<ParentCode, String> {
    Optional<ParentCode> findByParentCode(String parentCode);

    Optional<ParentCode> findByParentId(String parentId);
}

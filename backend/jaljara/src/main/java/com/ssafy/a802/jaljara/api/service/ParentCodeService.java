package com.ssafy.a802.jaljara.api.service;

import com.ssafy.a802.jaljara.db.entity.ParentCode;
import com.ssafy.a802.jaljara.db.repository.ParentCodeRepository;
import com.ssafy.a802.jaljara.exception.ExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class ParentCodeService {
    private final ParentCodeRepository parentCodeRepository;
    private final Random random = new Random();

    public ParentCode createParentCode(long parentId) {
        Optional<ParentCode> parentCode = parentCodeRepository.findByParentId(String.valueOf(parentId));
        if(parentCode.isPresent())
            return parentCode.get();

        return parentCodeRepository.save(ParentCode.builder()
                        .parentCode(String.format("%08d", random.nextInt(99999999)))
                        .parentId(String.valueOf(parentId))
                .build());
    }

    public ParentCode getParentCodeEntity(String parentCode) {
        return parentCodeRepository.findByParentCode(parentCode).orElseThrow(() -> ExceptionFactory.invalidParentCode());
    }
}

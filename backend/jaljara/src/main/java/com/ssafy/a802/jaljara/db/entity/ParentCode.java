package com.ssafy.a802.jaljara.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("ParentCode")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParentCode {
    @Id
    @Indexed
    private String parentCode;
    @Indexed
    private String parentId;
}

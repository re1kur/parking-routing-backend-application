package re1kur.is.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.is.entity.Code;
import re1kur.is.mapper.CodeMapper;

import java.time.Duration;
import java.time.Instant;

@Component
public class CodeMapperImpl implements CodeMapper {
    @Override
    public Code write(String id, String value, Integer cacheTtl) {
        return Code.builder()
                .id(id)
                .value(value)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofMinutes(cacheTtl)))
                .build();
    }
}

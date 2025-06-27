package re1kur.is.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import re1kur.is.entity.Code;
import re1kur.is.mapper.CodeMapper;
import re1kur.is.repository.cache.CodeRepository;
import re1kur.is.service.CodeService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final CodeRepository repo;
    private final CodeMapper mapper;

    @Value("${cache.code.ttl}")
    private Integer cacheTtl;


    @Override
    public void generateNew(String id) {
        String value = CodeGenerator.generateOTP();
        Code mapped = mapper.write(id, value, cacheTtl);

        repo.save(mapped);
        log.info("Generated code {} for id {}", value, mapped.getId());
    }
}

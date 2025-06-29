package re1kur.is.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import re1kur.core.exception.CodeHasExpiredException;
import re1kur.core.exception.CodeMismatchException;
import re1kur.core.exception.CodeNotFoundException;
import re1kur.is.entity.Code;
import re1kur.is.mapper.CodeMapper;
import re1kur.is.mq.EventPublisher;
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
    public String generateCode(String id) {
        String value = CodeGenerator.generateOTP();
        Code mapped = mapper.write(id, value, cacheTtl);
        repo.save(mapped);
        log.info("Generated code {} for id {}", value, mapped.getId());
        return value;
    }

    @Override
    public void validateCode(String id, String expected) {
        Code code = repo.findById(id).orElseThrow(() ->
                new CodeNotFoundException("Code for user %s was not found.".formatted(id)));
        if (code.isExpired()) {
            throw new CodeHasExpiredException("Code has expired. Generated new code and sent");
        }
        if (!code.isMatches(expected)) {
            throw new CodeMismatchException("Code mismatched with payload code %s.".formatted(expected));
        }
        repo.delete(code);
    }
}

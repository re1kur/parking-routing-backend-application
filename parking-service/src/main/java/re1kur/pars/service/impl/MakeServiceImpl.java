package re1kur.pars.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import re1kur.core.dto.MakeDto;
import re1kur.core.exception.MakeAlreadyExistsException;
import re1kur.core.exception.MakeNotFoundException;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.MakePayload;
import re1kur.pars.entity.Make;
import re1kur.pars.mapper.MakeMapper;
import re1kur.pars.repository.MakeRepository;
import re1kur.pars.service.MakeService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MakeServiceImpl implements MakeService {
    private final MakeRepository repo;
    private final MakeMapper mapper;

    @Override
    public MakeDto create(MakePayload payload, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("CREATE MAKE: {} by user [{}]", payload, sub);

        String name = payload.name();

        if (repo.existsByName(name))
            throw new MakeAlreadyExistsException("Make with name '%s' already exists.".formatted(name));

        Make mapped = mapper.write(payload);

        Make saved = repo.save(mapped);

        return mapper.read(saved);
    }

    @Override
    public MakeDto get(Integer makeId) {
        log.info("GET MAKE: ID [{}]", makeId);

        return repo.findById(makeId)
                .map(mapper::read)
                .orElseThrow(()-> new MakeNotFoundException(
                        "Make with ID [%d] was not found.".formatted(makeId)));
    }

    @Override
    public MakeDto update(MakePayload payload, Integer makeId, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("UPDATE MAKE: {} by user [{}]", payload, sub);

        String name = payload.name();

        Make found = repo.findById(makeId).orElseThrow(() -> new MakeNotFoundException(
                "Make with ID [%d] was not found.".formatted(makeId)));

        boolean nameEq = found.getName().equals(name);
        if (!nameEq) {
            if (repo.existsByName(name))
                throw new MakeAlreadyExistsException("Make with name '%s' already exists.".formatted(name));
        }

        Make updated = mapper.update(found, payload);

        repo.save(updated);

        return mapper.read(updated);
    }

    @Override
    public void delete(Integer makeId, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("DELETE MAKE: ID [{}] by user [{}]", makeId, sub);

        Make make = repo.findById(makeId).orElseThrow(() -> new MakeNotFoundException(
                "Make with ID [%d] was not found.".formatted(makeId)));

        repo.delete(make);
    }

    @Override
    public List<MakeDto> getPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable).map(mapper::read).stream().toList();
    }
}

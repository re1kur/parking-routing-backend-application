package re1kur.pars.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import re1kur.core.dto.RegionDto;
import re1kur.core.exception.RegionAlreadyExistsException;
import re1kur.core.exception.RegionNotFoundException;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.RegionPayload;
import re1kur.pars.entity.Region;
import re1kur.pars.mapper.RegionMapper;
import re1kur.pars.repository.RegionRepository;
import re1kur.pars.service.RegionService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regRepo;
    private final RegionMapper mapper;

    @Override
    public RegionDto create(RegionPayload payload, String bearer) {
        String userId = JwtExtractor.extractSubFromJwt(bearer);
        log.info("CREATE REGION [{}] by user [{}]", payload, userId);
        String name = payload.name();
        if (regRepo.existsByName(name))
            throw new RegionAlreadyExistsException("Region with name '%s' already exists.".formatted(name));

        Region mapped = mapper.create(payload);
        Region saved = regRepo.save(mapped);

        log.info("CREATED REGION [{}] by user [{}]", saved.getId(), userId);
        return mapper.read(saved);
    }

    @Override
    public List<RegionDto> getPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return regRepo.findAll(pageable).map(mapper::read).stream().toList();
    }

    @Override
    public RegionDto get(Integer regionId) {
        return regRepo.findById(regionId)
                .map(mapper::read)
                .orElseThrow(() -> new RegionNotFoundException(
                        "Region [%d] was not found.".formatted(regionId)));
    }

    @Override
    public RegionDto update(RegionPayload payload, Integer regionId, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("UPDATE REGION [{}] to {} by user [{}]", regionId, payload, sub);

        String name = payload.name();

        Region found = regRepo.findById(regionId).orElseThrow(() -> new RegionNotFoundException(
                "REGION [%d] was not found.".formatted(regionId)));

        boolean nameEq = found.getName().equals(name);
        if (!nameEq) {
            if (regRepo.existsByName(name))
                throw new RegionAlreadyExistsException("Region with name '%s' already exists.".formatted(name));
        }

        Region updated = mapper.update(found, payload);

        regRepo.save(updated);

        return mapper.read(updated);
    }

    @Override
    public void delete(Integer regionId, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("DELETE REGION [{}] by user [{}]", regionId, sub);

        Region make = regRepo.findById(regionId).orElseThrow(() -> new RegionNotFoundException(
                "REGION [%d] was not found.".formatted(regionId)));

        regRepo.delete(make);
    }
}

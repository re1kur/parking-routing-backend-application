package re1kur.pars.service.other.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.core.dto.PageDto;
import re1kur.core.dto.RegionCodeDto;
import re1kur.core.dto.RegionDto;
import re1kur.core.exception.RegionAlreadyExistsException;
import re1kur.core.exception.RegionCodeAlreadyExistsException;
import re1kur.core.exception.RegionNotFoundException;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.RegionCodePayload;
import re1kur.core.payload.RegionPayload;
import re1kur.pars.entity.region.Region;
import re1kur.pars.entity.region.RegionCode;
import re1kur.pars.mapper.RegionMapper;
import re1kur.pars.repository.RegionCodeRepository;
import re1kur.pars.repository.RegionRepository;
import re1kur.pars.service.other.RegionService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regRepo;
    private final RegionCodeRepository regCodeRepo;
    private final RegionMapper mapper;

    @Override
    @Transactional
    public RegionDto create(RegionPayload payload, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("CREATE REGION [{}] by user [{}]", payload, sub);
        String name = payload.name();
        if (regRepo.existsByName(name))
            throw new RegionAlreadyExistsException("Region [%s] already exists.".formatted(name));

        Region mapped = mapper.create(payload);
        Region saved = regRepo.save(mapped);

        log.info("CREATED REGION [{}] by user [{}]", saved.getId(), sub);
        return mapper.read(saved);
    }

    @Override
    public PageDto<RegionDto> getPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Region> found = regRepo.findAll(pageable);
        return mapper.readPage(found);
    }

    @Override
    public RegionDto get(Integer regionId) {
        return regRepo.findById(regionId)
                .map(mapper::read)
                .orElseThrow(() -> new RegionNotFoundException(
                        "Region [%d] was not found.".formatted(regionId)));
    }

    @Override
    @Transactional
    public RegionDto update(RegionPayload payload, Integer regionId, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("UPDATE REGION [{}] to {} by user [{}]", regionId, payload, sub);

        String name = payload.name();

        Region found = regRepo.findById(regionId).orElseThrow(() -> new RegionNotFoundException(
                "REGION [%d] was not found.".formatted(regionId)));

        boolean nameEq = found.getName().equals(name);
        if (!nameEq) {
            if (regRepo.existsByName(name))
                throw new RegionAlreadyExistsException("Region [%s] already exists.".formatted(name));
        }

        Region updated = mapper.update(found, payload);

        regRepo.save(updated);

        log.info("UPDATED REGION [{}] by user [{}]", regionId, sub);
        return mapper.read(updated);
    }

    @Override
    @Transactional
    public void delete(Integer regionId, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("DELETE REGION [{}] by user [{}]", regionId, sub);

        Region region = regRepo.findById(regionId).orElseThrow(() -> new RegionNotFoundException(
                "REGION [%d] was not found.".formatted(regionId)));

        region.getRegionCodes().clear();
        regRepo.delete(region);

        log.info("DELETED REGION [{}] by user [{}]", regionId, sub);
    }

    @Override
    @Transactional
    public RegionCodeDto createCode(Integer id, RegionCodePayload payload, String bearer) {
        UUID userId = UUID.fromString(JwtExtractor.extractSubFromJwt(bearer));
        String value = payload.codeValue();
        log.info("CREATE REGION CODE [{}] by user [{}]", value, userId);

        if (regCodeRepo.existsById(value))
            throw new RegionCodeAlreadyExistsException("Region code [%s] already exists.".formatted(value));

        Region found = regRepo.findById(id).orElseThrow(() -> new RegionNotFoundException(
                "REGION [%d] was not found.".formatted(id)));

        RegionCode mapped = mapper.createCode(payload, found);
        found.getRegionCodes().add(mapped);

        regRepo.save(found);

        return mapper.readCode(mapped);
    }
}

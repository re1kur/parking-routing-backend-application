package re1kur.pars.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.RegionDto;
import re1kur.core.payload.RegionCodePayload;
import re1kur.core.payload.RegionPayload;
import re1kur.pars.entity.Region;
import re1kur.pars.entity.RegionCode;
import re1kur.pars.mapper.RegionMapper;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RegionMapperImpl implements RegionMapper {


    @Override
    public Region create(RegionPayload payload) {
        Region region = Region.builder()
                .name(payload.name())
                .build();

        List<RegionCodePayload> codePayloads = payload.codes();
        if (codePayloads != null && !codePayloads.isEmpty()) {
            Set<RegionCode> codes = codePayloads.stream()
                    .map(cp -> RegionCode.builder()
                            .region(region)
                            .code(cp.codeValue())
                            .build())
                    .collect(Collectors.toSet());
            region.setRegionCodes(codes.stream().toList());
        }
        return region;
    }

    @Override
    public RegionDto read(Region saved) {
        Collection<RegionCode> codes = saved.getRegionCodes();
        List<String> codeList = null;
        if (codes != null) {
            codeList = new ArrayList<>(codes).stream()
                    .map(RegionCode::getCode)
                    .toList();
        }

        return RegionDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .codes(codeList)
                .build();
    }


    @Override
    public Region update(Region found, RegionPayload payload) {
        found.setName(payload.name());
        List<RegionCodePayload> codePayloads = payload.codes();

        if (codePayloads != null && !codePayloads.isEmpty()) {
            Set<RegionCode> codes = codePayloads.stream()
                    .map(cp -> RegionCode.builder()
                            .region(found)
                            .code(cp.codeValue())
                            .build())
                    .collect(Collectors.toCollection(HashSet::new));
            found.getRegionCodes().clear();
            found.getRegionCodes().addAll(codes);

        }

        return found;
    }

}

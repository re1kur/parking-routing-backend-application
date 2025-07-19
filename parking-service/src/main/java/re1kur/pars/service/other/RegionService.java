package re1kur.pars.service.other;

import re1kur.core.dto.RegionCodeDto;
import re1kur.core.dto.RegionDto;
import re1kur.core.payload.RegionCodePayload;
import re1kur.core.payload.RegionPayload;

import java.util.List;

public interface RegionService {
    RegionDto create(RegionPayload payload, String bearer);

    List<RegionDto> getPage(Integer page, Integer size);

    RegionDto get(Integer regionId);

    RegionDto update(RegionPayload payload, Integer regionId, String bearer);

    void delete(Integer regionId, String bearer);

    RegionCodeDto createCode(Integer id, RegionCodePayload payload, String bearer);
}

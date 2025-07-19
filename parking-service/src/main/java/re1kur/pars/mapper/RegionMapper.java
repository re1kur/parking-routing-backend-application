package re1kur.pars.mapper;

import re1kur.core.dto.RegionCodeDto;
import re1kur.core.dto.RegionDto;
import re1kur.core.payload.RegionCodePayload;
import re1kur.core.payload.RegionPayload;
import re1kur.pars.entity.Region;
import re1kur.pars.entity.RegionCode;

public interface RegionMapper {
    Region create(RegionPayload payload);

    RegionDto read(Region saved);

    Region update(Region found, RegionPayload payload);

    RegionCode createCode(RegionCodePayload payload, Region region);

    RegionCodeDto readCode(RegionCode mapped);
}

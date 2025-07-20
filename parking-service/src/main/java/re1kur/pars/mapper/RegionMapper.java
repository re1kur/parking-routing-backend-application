package re1kur.pars.mapper;

import org.springframework.data.domain.Page;
import re1kur.core.dto.PageDto;
import re1kur.core.dto.RegionCodeDto;
import re1kur.core.dto.RegionDto;
import re1kur.core.payload.RegionCodePayload;
import re1kur.core.payload.RegionPayload;
import re1kur.pars.entity.region.Region;
import re1kur.pars.entity.region.RegionCode;

public interface RegionMapper {
    Region create(RegionPayload payload);

    RegionDto read(Region saved);

    Region update(Region found, RegionPayload payload);

    RegionCode createCode(RegionCodePayload payload, Region region);

    RegionCodeDto readCode(RegionCode mapped);

    PageDto<RegionDto> readPage(Page<Region> found);
}

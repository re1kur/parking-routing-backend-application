package re1kur.pars.service.other;

import re1kur.core.dto.MakeDto;
import re1kur.core.dto.PageDto;
import re1kur.core.payload.MakePayload;

public interface MakeService {
    MakeDto create(MakePayload payload, String bearer);

    MakeDto get(Integer i);

    MakeDto update(MakePayload payload, Integer makeId, String bearer);

    void delete(Integer makeId, String bearer);

    PageDto<MakeDto> getPage(Integer page, Integer size);
}

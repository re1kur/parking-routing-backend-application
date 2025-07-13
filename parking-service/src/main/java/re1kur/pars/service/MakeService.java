package re1kur.pars.service;

import re1kur.core.dto.MakeDto;
import re1kur.core.payload.MakePayload;

import java.util.List;

public interface MakeService {
    MakeDto create(MakePayload payload, String bearer);

    MakeDto get(Integer i);

    MakeDto update(MakePayload payload, Integer makeId, String bearer);

    void delete(Integer makeId, String bearer);

    List<MakeDto> getPage(Integer page, Integer size);
}

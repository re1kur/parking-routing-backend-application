package re1kur.pars.service;

import re1kur.core.dto.MakeDto;
import re1kur.core.payload.MakePayload;

public interface MakeService {
    MakeDto create(MakePayload payload);

    MakeDto get(Integer i);

    MakeDto update(MakePayload payload, Integer makeId);

    void delete(Integer makeId);
}

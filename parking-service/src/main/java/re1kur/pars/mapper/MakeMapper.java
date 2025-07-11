package re1kur.pars.mapper;

import re1kur.core.dto.MakeDto;
import re1kur.core.payload.MakePayload;
import re1kur.pars.entity.Make;

public interface MakeMapper {
    Make write(MakePayload payload);

    MakeDto read(Make saved);

    Make update(Make found, MakePayload payload);
}

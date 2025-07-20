package re1kur.pars.mapper;

import org.springframework.data.domain.Page;
import re1kur.core.dto.MakeDto;
import re1kur.core.dto.PageDto;
import re1kur.core.payload.MakePayload;
import re1kur.pars.entity.make.Make;

public interface MakeMapper {
    Make write(MakePayload payload);

    MakeDto read(Make saved);

    Make update(Make found, MakePayload payload);

    PageDto<MakeDto> readPage(Page<Make> found);
}

package re1kur.pars.mapper.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import re1kur.core.dto.MakeDto;
import re1kur.core.dto.PageDto;
import re1kur.core.payload.MakePayload;
import re1kur.pars.entity.make.Make;
import re1kur.pars.mapper.MakeMapper;

@Component
public class MakeMapperImpl implements MakeMapper {
    @Override
    public Make write(MakePayload payload) {
        return Make.builder()
                .name(payload.name())
                .build();
    }

    @Override
    public MakeDto read(Make saved) {
        return MakeDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }

    @Override
    public Make update(Make found, MakePayload payload) {
        found.setName(payload.name());
        return found;
    }

    @Override
    public PageDto<MakeDto> readPage(Page<Make> found) {
        return new PageDto<>(
                found.getNumber(),
                found.getTotalPages(),
                found.getSize(),
                found.getContent().stream().map(this::read).toList());
    }
}

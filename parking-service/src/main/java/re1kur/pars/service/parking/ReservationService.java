package re1kur.pars.service.parking;

import org.springframework.data.domain.Pageable;
import re1kur.core.dto.PageDto;
import re1kur.core.dto.ReservationDto;
import re1kur.core.dto.ReservationFullDto;
import re1kur.core.payload.ReservationPayload;

import java.time.LocalDate;
import java.util.UUID;

public interface ReservationService {
    ReservationFullDto create(String token, ReservationPayload payload);

    PageDto<ReservationDto> getPageByUserId(String token, Pageable pageable, LocalDate date);

    ReservationDto getById(UUID id, String bearer);

    ReservationFullDto getFullById(UUID id, String bearer);

    void deleteById(UUID id, String bearer);
}

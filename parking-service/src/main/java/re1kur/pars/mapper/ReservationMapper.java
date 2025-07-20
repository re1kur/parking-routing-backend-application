package re1kur.pars.mapper;

import org.springframework.data.domain.Page;
import re1kur.core.dto.PageDto;
import re1kur.core.dto.ReservationDto;
import re1kur.core.dto.ReservationFullDto;
import re1kur.core.payload.ReservationPayload;
import re1kur.pars.entity.place.Place;
import re1kur.pars.entity.reservation.Reservation;

import java.util.UUID;

public interface ReservationMapper {
    Reservation create(ReservationPayload payload, UUID userId, Place parkingPlace);

    ReservationDto read(Reservation reservation);

    ReservationFullDto readFull(Reservation reservation);

    PageDto<ReservationDto> pageRead(Page<Reservation> found);
}

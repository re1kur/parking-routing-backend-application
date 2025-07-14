package re1kur.pars.mapper;

import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.payload.ParkingPlaceReservationPayload;
import re1kur.pars.entity.place.ParkingPlace;
import re1kur.pars.entity.place.Reservation;

import java.util.UUID;

public interface ReservationMapper {
    Reservation create(ParkingPlaceReservationPayload payload, UUID userId, ParkingPlace parkingPlace);

    ParkingPlaceReservationDto read(Reservation reservation);
}

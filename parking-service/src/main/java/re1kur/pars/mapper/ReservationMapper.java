package re1kur.pars.mapper;

import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.payload.ParkingPlaceReservationPayload;
import re1kur.pars.entity.place.Place;
import re1kur.pars.entity.place.Reservation;

import java.util.UUID;

public interface ReservationMapper {
    Reservation create(ParkingPlaceReservationPayload payload, UUID userId, Place parkingPlace);

    ParkingPlaceReservationDto read(Reservation reservation);
}

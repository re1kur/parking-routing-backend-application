package re1kur.pars.mapper;

import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.payload.ParkingPlaceReservationPayload;
import re1kur.pars.entity.ParkingPlace;
import re1kur.pars.entity.ParkingPlaceReservation;

import java.util.UUID;

public interface ReservationMapper {
    ParkingPlaceReservation create(ParkingPlaceReservationPayload payload, UUID userId, ParkingPlace parkingPlace);

    ParkingPlaceReservationDto read(ParkingPlaceReservation reservation);
}

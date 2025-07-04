package re1kur.pars.service;

import jakarta.validation.Valid;
import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.payload.ParkingPlaceReservationPayload;

public interface ReservationService {
    ParkingPlaceReservationDto create(String token, ParkingPlaceReservationPayload payload);
}

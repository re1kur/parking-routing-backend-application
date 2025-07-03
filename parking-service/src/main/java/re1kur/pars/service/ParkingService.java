package re1kur.pars.service;

import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceShortDto;
import re1kur.core.payload.ParkingPlacePayload;

public interface ParkingService {
    ParkingPlaceShortDto create(ParkingPlacePayload payload, String token);

    ParkingPlaceDto getByNumber(Integer number);

    ParkingPlaceFullDto getFullByNumber(Integer number);

    ParkingPlaceShortDto getShortByNumber(Integer number);
}

package re1kur.pars.service.parking;

import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceShortDto;
import re1kur.core.payload.ParkingPlacePayload;

import java.util.List;

public interface PlaceService {
    ParkingPlaceShortDto create(ParkingPlacePayload payload, String token);

    ParkingPlaceDto getByNumber(Integer number);

    ParkingPlaceFullDto getFullByNumber(Integer number);

    ParkingPlaceShortDto getShortByNumber(Integer number);

    ParkingPlaceDto clear(String token, Integer number);

    List<ParkingPlaceDto> getAll();
}

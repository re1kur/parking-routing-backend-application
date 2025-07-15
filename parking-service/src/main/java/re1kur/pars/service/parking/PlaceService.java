package re1kur.pars.service.parking;

import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.payload.ParkingPlacePayload;

import java.util.List;

public interface PlaceService {
    ParkingPlaceDto create(ParkingPlacePayload payload, String token);

    ParkingPlaceDto getByNumber(Integer number);

    ParkingPlaceFullDto getFullByNumber(Integer number);

    List<ParkingPlaceDto> getPage(Integer page, Integer size);

    ParkingPlaceDto update(Integer number, ParkingPlacePayload payload, String bearer);

    void delete(Integer number, String bearer);
}

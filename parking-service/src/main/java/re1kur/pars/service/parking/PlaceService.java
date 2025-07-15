package re1kur.pars.service.parking;

import re1kur.core.dto.PlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.payload.ParkingPlacePayload;

import java.util.List;

public interface PlaceService {
    PlaceDto create(ParkingPlacePayload payload, String token);

    PlaceDto getByNumber(Integer number);

    ParkingPlaceFullDto getFullByNumber(Integer number);

    List<PlaceDto> getPage(Integer page, Integer size);

    PlaceDto update(Integer number, ParkingPlacePayload payload, String bearer);

    void delete(Integer number, String bearer);

    List<PlaceDto> getAvailableListNow();

    List<PlaceDto> getAvailableListByDate(String date);

    List<PlaceDto> getAvailableListNowByNumber(Integer number);

    List<PlaceDto> getAvailableListByNumberAndDate(String date);
}

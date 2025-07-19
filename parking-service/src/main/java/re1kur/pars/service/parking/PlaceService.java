package re1kur.pars.service.parking;

import re1kur.core.dto.PlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.payload.PlacePayload;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PlaceService {
    PlaceDto create(PlacePayload payload, String token);

    PlaceDto getByNumber(Integer number);

    ParkingPlaceFullDto getFullByNumber(Integer number);

    List<PlaceDto> getPage(Integer page, Integer size);

    PlaceDto update(Integer number, PlacePayload payload, String bearer);

    void delete(Integer number, String bearer);

    List<Integer> getAvailablePlacesByNow();

    List<Integer> getAvailablePlacesByDate(LocalDate date);

    List<Integer> getAvailablePlacesByStartAndEnd(LocalDateTime startAt, LocalDateTime endAt);
}

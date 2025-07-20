package re1kur.pars.mapper;

import org.springframework.data.domain.Page;
import re1kur.core.dto.PageDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.PlaceDto;
import re1kur.core.payload.PlacePayload;
import re1kur.pars.entity.place.Place;

public interface ParkingMapper {
    Place create(PlacePayload payload);

    PlaceDto read(Place parkingPlace);

    ParkingPlaceFullDto readFull(Place parkingPlace);

    Place update(Place found, PlacePayload payload);

    PageDto<PlaceDto> readPage(Page<Place> found);
}

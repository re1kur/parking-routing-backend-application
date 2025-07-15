package re1kur.pars.mapper;

import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.PlaceDto;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.entity.place.Place;

public interface ParkingMapper {
    Place create(ParkingPlacePayload payload);

    PlaceDto read(Place parkingPlace);

    ParkingPlaceFullDto readFull(Place parkingPlace);

    Place update(Place found, ParkingPlacePayload payload);
}

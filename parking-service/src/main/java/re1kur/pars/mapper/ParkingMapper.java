package re1kur.pars.mapper;

import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceShortDto;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.entity.ParkingPlace;

public interface ParkingMapper {
    ParkingPlace create(ParkingPlacePayload payload);

    ParkingPlaceShortDto readShort(ParkingPlace parkingPlace);

    ParkingPlaceDto read(ParkingPlace parkingPlace);

    ParkingPlaceFullDto readFull(ParkingPlace parkingPlace);

    ParkingPlace clear(ParkingPlace parkingPlace);
}

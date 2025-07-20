package re1kur.pars.mapper;

import re1kur.core.dto.CarDto;
import re1kur.core.dto.CarFullDto;
import re1kur.core.dto.CarShortDto;
import re1kur.core.payload.CarPayload;
import re1kur.core.payload.CarUpdatePayload;
import re1kur.pars.entity.car.Car;
import re1kur.pars.entity.region.RegionCode;
import re1kur.pars.entity.make.Make;

import java.util.UUID;

public interface CarMapper {
    Car create(CarPayload payload, RegionCode regionCode, UUID ownerId);

    CarShortDto readShort(Car build);

    Car update(Car car, CarUpdatePayload payload, RegionCode regionCode, Make make);

    CarFullDto readFull(Car saved);

    CarDto read(Car car);
}

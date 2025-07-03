package re1kur.pars.mapper;

import re1kur.core.dto.CarDto;
import re1kur.core.dto.CarFullDto;
import re1kur.core.dto.CarShortDto;
import re1kur.core.payload.CarPayload;
import re1kur.core.payload.CarUpdatePayload;
import re1kur.pars.entity.Car;
import re1kur.pars.entity.Code;
import re1kur.pars.entity.Make;

import java.util.UUID;

public interface CarMapper {
    Car create(CarPayload payload, Code code, UUID ownerId);

    CarShortDto readShort(Car build);

    Car update(Car car, CarUpdatePayload payload, Code code, Make make);

    CarFullDto readFull(Car saved);

    CarDto read(Car car);
}

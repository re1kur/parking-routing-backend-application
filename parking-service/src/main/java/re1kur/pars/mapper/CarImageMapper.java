package re1kur.pars.mapper;

import re1kur.core.dto.CarImageDto;
import re1kur.core.payload.CarImagePayload;
import re1kur.pars.entity.car.Car;
import re1kur.pars.entity.car.CarImage;

public interface CarImageMapper {
    CarImage create(Car car, CarImagePayload payload);

    CarImageDto read(CarImage mapped);
}

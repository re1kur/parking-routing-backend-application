package re1kur.pars.mapper;

import re1kur.core.payload.CarPayload;
import re1kur.core.payload.CarUpdatePayload;
import re1kur.pars.entity.Car;
import re1kur.pars.entity.CarInformation;
import re1kur.pars.entity.Make;

public interface CarInformationMapper {
    CarInformation create(CarPayload payload, Make make, Car car);

    CarInformation update(CarInformation carInformation, CarUpdatePayload payload);
}

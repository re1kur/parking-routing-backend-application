package re1kur.pars.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.payload.CarPayload;
import re1kur.core.payload.CarUpdatePayload;
import re1kur.pars.entity.Car;
import re1kur.pars.entity.CarInformation;
import re1kur.pars.entity.Make;
import re1kur.pars.mapper.CarInformationMapper;

@Component
public class CarInformationMapperImpl implements CarInformationMapper {
    @Override
    public CarInformation create(CarPayload payload, Make make, Car car) {
        CarInformation build = CarInformation.builder()
                .car(car)
                .model(payload.model())
                .make(make)
                .color(payload.color())
                .build();
        car.setCarInformation(build);
        return build;
    }

    @Override
    public CarInformation update(CarInformation carInformation, CarUpdatePayload payload) {
        carInformation.setMake(null);
        carInformation.setModel(payload.model());
        carInformation.setColor(payload.color());
        return carInformation;
    }
}

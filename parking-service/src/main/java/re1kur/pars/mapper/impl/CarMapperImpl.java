package re1kur.pars.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import re1kur.core.dto.CarDto;
import re1kur.core.dto.CarFullDto;
import re1kur.core.dto.CarShortDto;
import re1kur.core.payload.CarPayload;
import re1kur.core.payload.CarUpdatePayload;
import re1kur.pars.entity.Car;
import re1kur.pars.entity.CarInformation;
import re1kur.pars.entity.Code;
import re1kur.pars.entity.Make;
import re1kur.pars.mapper.CarMapper;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CarMapperImpl implements CarMapper {

    @Override
    public Car create(CarPayload payload, Code code, UUID ownerId) {
        return Car.builder()
                .ownerId(ownerId)
                .licensePlate(payload.licensePlate())
                .regionCode(code)
                .build();
    }

    @Override
    public CarShortDto readShort(Car build) {
        return CarShortDto.builder()
                .id(build.getId())
                .ownerId(build.getOwnerId())
                .licensePlate(build.getLicensePlate())
                .regionCode(build.getRegionCode().getCode())
                .build();
    }

    @Override
    public Car update(Car car, CarUpdatePayload payload, Code code, Make make) {
        CarInformation carInformation = car.getCarInformation();
        carInformation.setModel(payload.model());
        carInformation.setColor(payload.color());
        carInformation.setMake(make);

        car.setRegionCode(code);
        car.setLicensePlate(payload.licensePlate());
        return car;
    }

    @Override
    public CarFullDto readFull(Car saved) {
        CarInformation carInformation = saved.getCarInformation();
        return CarFullDto.builder()
                .carId(saved.getId())
                .ownerId(saved.getOwnerId())
                .licensePlate(saved.getLicensePlate())
                .regionCode(saved.getRegionCode().getCode())
                .regionName(saved.getRegionCode().getRegion().getName())
                .color(carInformation.getColor())
                .makeId(carInformation.getMake().getId())
                .model(carInformation.getModel())
                .build();
    }

    @Override
    public CarDto read(Car car) {
        CarInformation carInformation = car.getCarInformation();
        return CarDto.builder()
                .carId(car.getId())
                .ownerId(car.getOwnerId())
                .licensePlate(car.getLicensePlate())
                .regionCode(car.getRegionCode().getCode())
                .makeName(carInformation.getMake().getName())
                .model(carInformation.getModel())
                .titleImageId(null)
                .build();
    }
}

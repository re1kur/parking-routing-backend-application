package re1kur.pars.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.CarImageDto;
import re1kur.core.payload.CarImagePayload;
import re1kur.pars.entity.car.Car;
import re1kur.pars.entity.car.CarImage;
import re1kur.pars.mapper.CarImageMapper;

@Component
public class CarImageMapperImpl implements CarImageMapper {
    @Override
    public CarImage create(Car car, CarImagePayload payload) {
        return CarImage.builder()
                .car(car)
                .fileId(payload.fileId())
                .build();
    }

    @Override
    public CarImageDto read(CarImage mapped) {
        return CarImageDto.builder()
                .carId(mapped.getCar().getId())
                .fileId(mapped.getFileId())
                .build();
    }
}

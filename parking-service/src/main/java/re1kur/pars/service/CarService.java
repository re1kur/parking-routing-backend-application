package re1kur.pars.service;

import re1kur.core.dto.CarDto;
import re1kur.core.dto.CarFullDto;
import re1kur.core.dto.CarShortDto;
import re1kur.core.payload.CarPayload;
import re1kur.core.payload.CarUpdatePayload;

import java.util.List;
import java.util.UUID;

public interface CarService {
    CarShortDto create(CarPayload payload, String token);

    CarFullDto update(CarUpdatePayload payload, String token);

    CarShortDto getShort(UUID id);

    CarFullDto getFull(UUID id);

    void delete(UUID id, String token);

    List<CarDto> getCarsByToken(String token);
}

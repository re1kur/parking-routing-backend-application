package re1kur.pars.service;

import re1kur.core.dto.CarImageDto;
import re1kur.core.payload.CarImagePayload;

import java.util.UUID;

public interface CarImageService {
    CarImageDto create(UUID carId, CarImagePayload payload, String bearer);

    void delete(UUID carId, String fileId, String bearer);
}

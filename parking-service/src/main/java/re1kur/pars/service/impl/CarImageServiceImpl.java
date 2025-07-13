package re1kur.pars.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import re1kur.core.dto.CarImageDto;
import re1kur.core.exception.CarNotFoundException;
import re1kur.core.exception.FileNotFoundException;
import re1kur.core.exception.UserDoesNotHavePermissionForEndpoint;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.CarImagePayload;
import re1kur.pars.entity.Car;
import re1kur.pars.entity.CarImage;
import re1kur.pars.mapper.CarImageMapper;
import re1kur.pars.mq.EventPublisher;
import re1kur.pars.repository.CarImageRepository;
import re1kur.pars.repository.CarRepository;
import re1kur.pars.service.CarImageService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarImageServiceImpl implements CarImageService {
    private final CarImageRepository imgRepo;
    private final CarRepository carRepo;
    private final CarImageMapper imgMapper;
    private final EventPublisher publisher;

    @Override
    public CarImageDto create(UUID carId, CarImagePayload payload, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        UUID userId = UUID.fromString(sub);
        log.info("CREATE CAR-IMAGE REQUEST: user [{}] to car [{}]", userId, carId);

        Car found = carRepo.findById(carId).orElseThrow(() ->
                new CarNotFoundException("Car with ID [%s] was not found.".formatted(carId)));

        if (!found.isOwner(userId))
            throw new UserDoesNotHavePermissionForEndpoint(
                    ("User with ID [%s] does not have permission to perform" +
                            " actions with car that does not belong to him.").formatted(userId));

        CarImage mapped = imgMapper.create(found, payload);

        imgRepo.save(mapped);

        log.info("CAR-IMAGE CREATED: user [{}] to car [{}] an image [{}]", userId, carId, mapped.getFileId());
        return imgMapper.read(mapped);
    }

    @Override
    public void delete(UUID carId, String fileId, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        UUID userId = UUID.fromString(sub);
        log.info("DELETE CAR-IMAGE REQUEST: user [{}] to car [{}] an image [{}]", userId, carId, fileId);

        Car found = carRepo.findById(carId).orElseThrow(() ->
                new CarNotFoundException("Car with ID '%s' not found.".formatted(carId)));

        if (!found.isOwner(userId))
            throw new UserDoesNotHavePermissionForEndpoint(
                    ("User with ID [%s] does not have permission to perform" +
                            " actions with car that does not belong to him.").formatted(userId));

        CarImage carImage = imgRepo.findByFileId(fileId).orElseThrow(() ->
                new FileNotFoundException("Image [%s] was not found.".formatted(fileId)));

        imgRepo.delete(carImage);
        publisher.deleteFileById(fileId);
        log.info("CAR-IMAGE DELETED: user [{}] to car [{}] an image [{}]", userId, carId, fileId);
    }
}

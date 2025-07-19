package re1kur.pars.service.car.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.core.dto.CarDto;
import re1kur.core.dto.CarFullDto;
import re1kur.core.dto.CarShortDto;
import re1kur.core.exception.*;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.CarPayload;
import re1kur.core.payload.CarUpdatePayload;
import re1kur.pars.entity.car.Car;
import re1kur.pars.entity.car.CarInformation;
import re1kur.pars.entity.RegionCode;
import re1kur.pars.entity.Make;
import re1kur.pars.mapper.CarInformationMapper;
import re1kur.pars.mapper.CarMapper;
import re1kur.pars.repository.CarInformationRepository;
import re1kur.pars.repository.CarRepository;
import re1kur.pars.repository.RegionCodeRepository;
import re1kur.pars.repository.MakeRepository;
import re1kur.pars.service.car.CarService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarMapper carMapper;
    private final CarRepository carRepo;
    private final CarInformationRepository carInfoRepo;
    private final RegionCodeRepository codeRepo;
    private final CarInformationMapper carInfoMapper;
    private final MakeRepository makeRepo;

    @Override
    @Transactional
    public CarShortDto create(CarPayload payload, String token) {
        String sub = JwtExtractor.extractSubFromJwt(token);
        UUID ownerId = UUID.fromString(sub);
        String plate = payload.licensePlate();
        String code = payload.regionCode();
        Integer makeId = payload.makeId();

        log.info("CREATE CAR {} by user [{}]", payload, sub);

        RegionCode regionCode = codeRepo.findById(code).orElseThrow(() ->
                new CodeNotFoundException("Region code '%s' not found.".formatted(code)));
        Make make = makeRepo.findById(makeId).orElseThrow(() ->
                new MakeNotFoundException("Make with ID '%d' not found.".formatted(makeId)));
        if (carRepo.existsByLicensePlateAndRegionCode(plate, code))
            throw new CarAlreadyExistsException("Car with license plate %s[%s] already registered.".formatted(plate, code));

        Car car = carMapper.create(payload, regionCode, ownerId);
        Car saved = carRepo.save(car);

        CarInformation carInfoMapped = carInfoMapper.create(payload, make, saved);
        carInfoRepo.save(carInfoMapped);

        CarShortDto registered = carMapper.readShort(saved);
        log.info("CREATED CAR [{}] by user [{}]", registered.id(), sub);

        return registered;
    }

    @Override
    @Transactional
    public CarFullDto update(UUID carId, CarUpdatePayload payload, String token) {
        String sub = JwtExtractor.extractSubFromJwt(token);
        UUID userId = UUID.fromString(sub);
        String regionCode = payload.regionCode();
        Integer makeId = payload.makeId();

        log.info("Received request from user with ID '{}' to edit car: {} ", sub, payload);

        Car found = carRepo.findById(carId).orElseThrow(() ->
                new CarNotFoundException("Car with ID '%s' not found.".formatted(carId)));

        if (!userId.equals(found.getOwnerId())) throw new UserDoesNotHavePermissionForEndpoint(
                "User '%s' does not have permission to edit not own car.".formatted(userId));

        RegionCode code = found.getRegionCode();
        Make make = found.getCarInformation().getMake();

        if (!found.getRegionCode().getCode().equals(regionCode)) {
            code = codeRepo.findById(regionCode).orElseThrow(() ->
                    new CodeNotFoundException("Code '%s' not found.".formatted(regionCode)));
        }
        if (!found.getCarInformation().getMake().getId().equals(makeId)) {
            make = makeRepo.findById(makeId).orElseThrow(() ->
                    new MakeNotFoundException("Make with ID '%d' not found.".formatted(makeId)));
        }

        Car updated = carMapper.update(found, payload, code, make);

        CarFullDto dto = carMapper.readFull(updated);
        log.info("Car updated: {}", dto.toString());

        return dto;
    }

    @Override
    public CarShortDto getShort(UUID carId, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("GET SHORT CAR [{}] by user [{}]", carId, sub);

        Car car = carRepo.findById(carId).orElseThrow(() ->
                new CarNotFoundException("Car with ID '%s' not found.'".formatted(carId)));

        CarShortDto dto = carMapper.readShort(car);
        log.info("Response short-dto car: {}", dto.toString());

        return dto;
    }

    @Override
    public CarFullDto getFull(UUID carId, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("GET FULL CAR [{}] by user [{}]", carId, sub);

        Car car = carRepo.findById(carId).orElseThrow(() ->
                new CarNotFoundException("Car with ID '%s' not found.".formatted(carId)));

        CarFullDto dto = carMapper.readFull(car);
        log.info("Response full-dto car: {}", dto.toString());

        return dto;
    }

    @Override
    @Transactional
    public void delete(UUID carId, String bearer) {
        UUID userId = UUID.fromString(JwtExtractor.extractSubFromJwt(bearer));

        log.info("DELETE CAR [{}] by user[{}]", carId, userId);

        Car found = carRepo.findById(carId).orElseThrow(() ->
                new CarNotFoundException("Car with ID '%s' not found.".formatted(carId)));
        if (!userId.equals(found.getOwnerId()))
            throw new UserDoesNotHavePermissionForEndpoint(("User [%s] does not have permission " +
                    "to perform actions with car that does not belong to him.").formatted(userId));

        found.setCarInformation(null);
        found.getImages().clear();
        carRepo.delete(found);
        log.info("DELETED CAR [{}] by user [{}].", carId, userId);
    }

    @Override
    public List<CarDto> getCarsByOwner(String token) {
        String sub = JwtExtractor.extractSubFromJwt(token);
        UUID userId = UUID.fromString(sub);

        return carRepo.findAllByOwnerId(userId).stream().map(carMapper::read).toList();
    }

    @Override
    public CarDto getById(UUID carId, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("GET CAR [{}] by user [{}]", carId, sub);

        Car found = carRepo.findById(carId).orElseThrow(() ->
                new CarNotFoundException("Car with ID '%s' not found.".formatted(carId)));

        return carMapper.read(found);
    }
}

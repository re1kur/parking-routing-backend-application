package re1kur.pars.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceShortDto;
import re1kur.core.exception.ParkingPlaceAlreadyExistsException;
import re1kur.core.exception.ParkingPlaceNotFoundException;
import re1kur.core.exception.UserNotFoundException;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.client.IdentityClient;
import re1kur.pars.entity.ParkingPlace;
import re1kur.pars.mapper.ParkingMapper;
import re1kur.pars.repository.ParkingPlaceRepository;
import re1kur.pars.service.ParkingService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {
    private final ParkingMapper mapper;
    private final ParkingPlaceRepository parkingRepo;
    private final IdentityClient identityClient;

    @Override
    public ParkingPlaceShortDto create(ParkingPlacePayload payload, String token) {
        String sub = JwtExtractor.extractSubFromJwt(token);
        UUID userId = UUID.fromString(sub);
        Integer number = payload.number();

        if (!identityClient.isExistsById(userId))
            throw new UserNotFoundException("User '%s' not found.".formatted(sub));
        log.info("Received request from user with ID '{}' to create parking place: {} ", sub, payload);

        if (parkingRepo.existsById(number))
            throw new ParkingPlaceAlreadyExistsException("Parking place №%d already exists.".formatted(number));
        ParkingPlace mapped = mapper.create(payload);
        parkingRepo.save(mapped);

        ParkingPlaceShortDto dto = mapper.readShort(mapped);
        log.info("Created parking place: {}", dto.toString());
        return dto;
    }

    @Override
    public ParkingPlaceDto getByNumber(Integer number) {
        ParkingPlace parkingPlace = parkingRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place №%d not found.".formatted(number)));

        return mapper.read(parkingPlace);
    }

    @Override
    public ParkingPlaceFullDto getFullByNumber(Integer number) {
        ParkingPlace parkingPlace = parkingRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place №%d not found.".formatted(number)));

        return mapper.readFull(parkingPlace);
    }

    @Override
    public ParkingPlaceShortDto getShortByNumber(Integer number) {
        ParkingPlace parkingPlace = parkingRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place №%d not found.".formatted(number)));

        return mapper.readShort(parkingPlace);
    }
}

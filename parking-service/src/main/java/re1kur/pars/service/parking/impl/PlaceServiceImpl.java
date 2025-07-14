package re1kur.pars.service.parking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceShortDto;
import re1kur.core.exception.ParkingPlaceAlreadyExistsException;
import re1kur.core.exception.ParkingPlaceNotFoundException;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.entity.place.ParkingPlace;
import re1kur.pars.mapper.ParkingMapper;
import re1kur.pars.repository.ParkingPlaceRepository;
import re1kur.pars.service.parking.PlaceService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final ParkingMapper placeMapper;
    private final ParkingPlaceRepository placeRepo;

    @Override
    public ParkingPlaceShortDto create(ParkingPlacePayload payload, String bearer) {
        UUID userId = UUID.fromString(JwtExtractor.extractSubFromJwt(bearer));
        log.info("CREATE PARKING-PLACE: user [{}] with payload: {} ", userId, payload);
        Integer number = payload.number();

        if (placeRepo.existsById(number))
            throw new ParkingPlaceAlreadyExistsException("Parking place №%d already exists.".formatted(number));

        ParkingPlace mapped = placeMapper.create(payload);

        ParkingPlace saved = placeRepo.save(mapped);
        log.info("PARKING-PLACE [{}] CREATED.", saved.getNumber());

        return placeMapper.readShort(saved);
    }

    @Override
    public ParkingPlaceDto getByNumber(Integer number) {
        ParkingPlace parkingPlace = placeRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place №%d not found.".formatted(number)));

        return placeMapper.read(parkingPlace);
    }

    @Override
    public ParkingPlaceShortDto getShortByNumber(Integer number) {
        ParkingPlace parkingPlace = placeRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place №%d not found.".formatted(number)));

        return placeMapper.readShort(parkingPlace);
    }

    @Override
    public ParkingPlaceFullDto getFullByNumber(Integer number) {
        ParkingPlace parkingPlace = placeRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place №%d not found.".formatted(number)));

        return placeMapper.readFull(parkingPlace);
    }

    @Override
    public ParkingPlaceDto clear(String token, Integer number) {
//        String sub = JwtExtractor.extractSubFromJwt(token);
//        UUID userId = UUID.fromString(sub);
//
//        log.info("Received request from user with ID '{}' to clear parking place №{}.", userId, number);
//
//        ParkingPlace parkingPlace = placeRepo.findById(number).orElseThrow(() ->
//                new ParkingPlaceNotFoundException("Parking place №%d not found.".formatted(number)));
//
//        ParkingPlace cleared = placeMapper.clear(parkingPlace);
//        placeRepo.save(cleared);
//
//        ParkingPlaceDto dto = placeMapper.read(cleared);
//        log.info("Cleared parking place: {}", dto.toString());

//        return dto;

//        TODO: reimagine method

        return null;
    }

    @Override
    public List<ParkingPlaceDto> getAll() {
        return ((List<ParkingPlace>) placeRepo.findAll()).stream().map(placeMapper::read).toList();
    }


}

package re1kur.pars.service.parking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.exception.ParkingPlaceAlreadyExistsException;
import re1kur.core.exception.ParkingPlaceNotFoundException;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.entity.place.Place;
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
    public ParkingPlaceDto create(ParkingPlacePayload payload, String bearer) {
        UUID userId = UUID.fromString(JwtExtractor.extractSubFromJwt(bearer));
        log.info("CREATE PARKING-PLACE [{}] by user [{}].", payload, userId);
        Integer number = payload.number();

        if (placeRepo.existsById(number))
            throw new ParkingPlaceAlreadyExistsException("Parking place [%d] already exists.".formatted(number));

        Place mapped = placeMapper.create(payload);

        Place saved = placeRepo.save(mapped);

        log.info("PARKING-PLACE [{}] CREATED.", saved.getNumber());
        return placeMapper.read(saved);
    }

    @Override
    public ParkingPlaceDto getByNumber(Integer number) {
        Place parkingPlace = placeRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place [%d] not found.".formatted(number)));

        return placeMapper.read(parkingPlace);
    }

    @Override
    public ParkingPlaceFullDto getFullByNumber(Integer number) {
        Place parkingPlace = placeRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place [%d] not found.".formatted(number)));

        return placeMapper.readFull(parkingPlace);
    }

    @Override
    public List<ParkingPlaceDto> getPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return placeRepo.findAll(pageable).map(placeMapper::read).getContent();
        // todo: reimagine way to return info about place
    }

    @Override
    public ParkingPlaceDto update(Integer number, ParkingPlacePayload payload, String bearer) {
        return null;
        // todo: update method
    }

    @Override
    public void delete(Integer number, String bearer) {
        // todo: delete method
    }


}

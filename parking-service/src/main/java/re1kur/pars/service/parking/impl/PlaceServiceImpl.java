package re1kur.pars.service.parking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.PlaceDto;
import re1kur.core.exception.PlaceAlreadyExistsException;
import re1kur.core.exception.PlaceNotFoundException;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.PlacePayload;
import re1kur.pars.entity.place.Place;
import re1kur.pars.mapper.ParkingMapper;
import re1kur.pars.repository.PlaceRepository;
import re1kur.pars.service.parking.PlaceService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final ParkingMapper placeMapper;
    private final PlaceRepository placeRepo;

    @Override
    @Transactional
    public PlaceDto create(PlacePayload payload, String bearer) {
        UUID userId = UUID.fromString(JwtExtractor.extractSubFromJwt(bearer));
        BigDecimal latitude = payload.latitude();
        BigDecimal longitude = payload.longitude();
        Integer number = payload.number();
        log.info("CREATE PARKING-PLACE [{}] by user [{}].", payload, userId);

        if (placeRepo.existsById(number))
            throw new PlaceAlreadyExistsException("Parking place [%d] already exists.".formatted(number));
        if (placeRepo.existsByLatitudeAndLongitude(latitude, longitude)) {
            throw new PlaceAlreadyExistsException(("Parking place with latitude [%f] " +
                    "and longitude [%f] already exists.").formatted(latitude, longitude));
        }

        Place mapped = placeMapper.create(payload);

        Place saved = placeRepo.save(mapped);

        log.info("PARKING-PLACE [{}] CREATED.", saved.getNumber());
        return placeMapper.read(saved);
    }

    @Override
    public PlaceDto getByNumber(Integer number) {
        Place parkingPlace = placeRepo.findById(number).orElseThrow(() ->
                new PlaceNotFoundException("Parking place [%d] not found.".formatted(number)));

        return placeMapper.read(parkingPlace);
    }

    @Override
    public ParkingPlaceFullDto getFullByNumber(Integer number) {
        Place parkingPlace = placeRepo.findById(number).orElseThrow(() ->
                new PlaceNotFoundException("Parking place [%d] not found.".formatted(number)));

        return placeMapper.readFull(parkingPlace);
    }

    @Override
    public List<PlaceDto> getPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return placeRepo.findAll(pageable).map(placeMapper::read).getContent();
        // todo: reimagine way to return info about place
    }

    @Override
    @Transactional
    public PlaceDto update(Integer number, PlacePayload payload, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("UPDATE PARKING-PLACE [{}] by user [{}]", payload, sub);

        Place found = placeRepo.findById(number).orElseThrow(() ->
                new PlaceNotFoundException("Parking place [%d] not found.".formatted(number)));

        checkConflicts(found, payload);

        Place mapped = placeMapper.update(found, payload);

        placeRepo.save(mapped);

        return placeMapper.read(mapped);
    }

    private void checkConflicts(Place found, PlacePayload payload) {
        BigDecimal payloadLatitude = payload.latitude();
        BigDecimal payloadLongitude = payload.longitude();

        BigDecimal latitude = found.getLatitude();
        BigDecimal longitude = found.getLongitude();

        boolean latitudeEq = latitude.compareTo(payloadLatitude) == 0;
        boolean longitudeEq = longitude.compareTo(payloadLongitude) == 0;

        if (!(latitudeEq && longitudeEq)) {
            if (placeRepo.existsByLatitudeAndLongitude(payloadLatitude, payloadLongitude)) {
                throw new PlaceAlreadyExistsException((
                        "Parking place with latitude [%s] and longitude [%s] already exists."
                ).formatted(payloadLatitude, payloadLongitude));
            }
        }
    }


    @Override
    @Transactional
    public void delete(Integer number, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("DELETE PARKING-PLACE [{}] by user [{}]", number, sub);

        Place found = placeRepo.findById(number).orElseThrow(() ->
                new PlaceNotFoundException("Parking place [%d] not found.".formatted(number)));

        placeRepo.delete(found);
        log.info("DELETED PARKING-PLACE [{}] by user [{}]", number, sub);
    }

    @Override
    public List<Integer> getAvailablePlacesByNow() {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return placeRepo.findAvailableNumbersByDate(startOfDay, endOfDay);
    }

    @Override
    public List<Integer> getAvailablePlacesByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return placeRepo
                .findAvailableNumbersByDate(startOfDay, endOfDay);
    }

    @Override
    public List<Integer> getAvailablePlacesByStartAndEnd(LocalDateTime startAt, LocalDateTime endAt) {
        return placeRepo
                .findAvailableNumbersByStartAndEnd(startAt, endAt);
    }
}
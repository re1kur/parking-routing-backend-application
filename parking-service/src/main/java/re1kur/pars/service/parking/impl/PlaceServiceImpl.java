package re1kur.pars.service.parking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.PlaceDto;
import re1kur.core.exception.ParkingPlaceAlreadyExistsException;
import re1kur.core.exception.ParkingPlaceNotFoundException;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.entity.place.Place;
import re1kur.pars.mapper.ParkingMapper;
import re1kur.pars.repository.ParkingPlaceRepository;
import re1kur.pars.service.parking.PlaceService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final ParkingMapper placeMapper;
    private final ParkingPlaceRepository placeRepo;

    @Override
    public PlaceDto create(ParkingPlacePayload payload, String bearer) {
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
    public PlaceDto getByNumber(Integer number) {
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
    public List<PlaceDto> getPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return placeRepo.findAll(pageable).map(placeMapper::read).getContent();
        // todo: reimagine way to return info about place
    }

    @Override
    public PlaceDto update(Integer number, ParkingPlacePayload payload, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("UPDATE PARKING-PLACE [{}] by user [{}]", payload, sub);

        Place found = placeRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place [%d] not found.".formatted(number)));

        checkConflicts(found, payload);

        Place updated = placeMapper.update(found, payload);

        placeRepo.save(updated);

        return placeMapper.read(updated);
    }

    private void checkConflicts(Place found, ParkingPlacePayload payload) {
        Float payloadLatitude = payload.latitude();
        Float payloadLongitude = payload.longitude();
        Integer payloadNumber = payload.number();

        Integer number = found.getNumber();
        Float latitude = found.getLatitude();
        Float longitude = found.getLongitude();

        boolean numberEq = Objects.equals(number, payloadNumber);
        if (!numberEq) {
            if (placeRepo.existsById(number))
                throw new ParkingPlaceAlreadyExistsException("Parking place [%d] already exists.".formatted(number));
        }

        boolean latitudeEq = Objects.equals(latitude, payloadLatitude);
        boolean longitudeEq = Objects.equals(longitude, payloadLongitude);

        if (!(latitudeEq || longitudeEq)) {
            if (placeRepo.existsByLatitudeAndLongitude(payloadLatitude, payloadLongitude))
                throw new ParkingPlaceAlreadyExistsException(("Parking place with latitude [%f] " +
                        "and longitude [%f] already exists.").formatted(payloadLatitude, payloadLongitude));
        }
    }

    @Override
    public void delete(Integer number, String bearer) {
        String sub = JwtExtractor.extractSubFromJwt(bearer);
        log.info("DELETE PARKING-PLACE [{}] by user [{}]", number, sub);

        Place found = placeRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place [%d] not found.".formatted(number)));

        placeRepo.delete(found);
        log.info("DELETED PARKING-PLACE [{}] by user [{}]", number, sub);
    }

    @Override
    public List<PlaceDto> getAvailableListNow() {
        LocalDate today = LocalDate.now();
        List<Place> body = placeRepo.findAllAvailableNumbersByDate(today);
        return body.stream().map(placeMapper::read).toList();
    }

    @Override
    public List<PlaceDto> getAvailableListByDate(String date) {
        //todo: getAvailableListByDate
        return null;
    }

    @Override
    public List<PlaceDto> getAvailableListNowByNumber(Integer number) {
        //todo: getAvailableListNowForNumber
        return null;
    }

    @Override
    public List<PlaceDto> getAvailableListByNumberAndDate(String date) {
        //todo: getAvailableListNowByNumberAndDate
        return null;
    }


}

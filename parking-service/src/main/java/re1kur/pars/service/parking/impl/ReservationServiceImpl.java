package re1kur.pars.service.parking.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.dto.PlaceReservationsDto;
import re1kur.core.exception.ParkingPlaceNotFoundException;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.ParkingPlaceReservationPayload;
import re1kur.pars.entity.place.Place;
import re1kur.pars.entity.place.Reservation;
import re1kur.pars.mapper.ReservationMapper;
import re1kur.pars.repository.ParkingPlaceRepository;
import re1kur.pars.repository.ReservationRepository;
import re1kur.pars.service.parking.ReservationService;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ParkingPlaceRepository parkingRepo;
    private final ReservationRepository reservationRepo;
    private final ReservationMapper mapper;

    @Override
    public ParkingPlaceReservationDto create(String token, ParkingPlaceReservationPayload payload) {
        String sub = JwtExtractor.extractSubFromJwt(token);
        UUID userId = UUID.fromString(sub);

        Integer number = payload.placeNumber();
        Place parkingPlace = parkingRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place №%d not found.".formatted(number)));

        Reservation mapped = mapper.create(payload, userId, parkingPlace);
        Reservation saved = reservationRepo.save(mapped);

        return mapper.read(saved);
    }

    @Override
    public List<PlaceReservationsDto> getListForToday() {
        List<Integer> numbers = parkingRepo.findAllNumbers();

        List<PlaceReservationsDto> reservations = new ArrayList<>();
        for (Integer number : numbers) {
            List<ParkingPlaceReservationDto> list = reservationRepo.findAllByNumberToday(number).stream().map(mapper::read).toList();
            PlaceReservationsDto dto = new PlaceReservationsDto(number, list);
            reservations.add(dto);
        }
        return reservations;
    }

    @Override
    public List<ParkingPlaceReservationDto> getListByPlaceNumberForToday(Integer number) {
        return reservationRepo.findAllByNumberToday(number)
                .stream().map(mapper::read)
                .toList();
    }

    @Override
    public List<PlaceReservationsDto> getListForDate(OffsetDateTime date) {
        List<Integer> numbers = parkingRepo.findAllNumbers();

        List<PlaceReservationsDto> reservations = new ArrayList<>();
        for (Integer number : numbers) {
            List<ParkingPlaceReservationDto> list = reservationRepo.findAllByNumberForDate(number, date)
                    .stream().map(mapper::read)
                    .toList();
            PlaceReservationsDto dto = new PlaceReservationsDto(number, list);
            reservations.add(dto);
        }
        return reservations;
    }

    @Override
    public Page<ParkingPlaceReservationDto> getPageByUserId(String token, Pageable pageable) {
        String sub = JwtExtractor.extractSubFromJwt(token);
        UUID userId = UUID.fromString(sub);

        return reservationRepo
                .findAllByOccupantUserId(userId, pageable)
                .map(mapper::read);
    }

    @Override
    public Page<ParkingPlaceReservationDto> getPageByUserIdAndDate(String token, OffsetDateTime date, Pageable pageable) {
        String sub = JwtExtractor.extractSubFromJwt(token);
        UUID userId = UUID.fromString(sub);

        return reservationRepo
                .findAllByOccupantUserIdAndDate(userId, pageable, date)
                .map(mapper::read);
    }
}

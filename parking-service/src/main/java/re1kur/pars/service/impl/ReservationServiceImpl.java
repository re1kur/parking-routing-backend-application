package re1kur.pars.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.exception.ParkingPlaceNotFoundException;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.ParkingPlaceReservationPayload;
import re1kur.pars.entity.ParkingPlace;
import re1kur.pars.entity.ParkingPlaceReservation;
import re1kur.pars.mapper.ReservationMapper;
import re1kur.pars.repository.ParkingPlaceRepository;
import re1kur.pars.repository.ReservationRepository;
import re1kur.pars.service.ReservationService;

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
        ParkingPlace parkingPlace = parkingRepo.findById(number).orElseThrow(() ->
                new ParkingPlaceNotFoundException("Parking place №%d not found.".formatted(number)));

        ParkingPlaceReservation mapped = mapper.create(payload, userId, parkingPlace);
        ParkingPlaceReservation saved = reservationRepo.save(mapped);

        return mapper.read(saved);
    }
}

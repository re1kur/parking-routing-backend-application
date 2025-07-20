package re1kur.pars.service.parking.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.core.dto.PageDto;
import re1kur.core.dto.ReservationDto;
import re1kur.core.dto.ReservationFullDto;
import re1kur.core.exception.PlaceNotFoundException;
import re1kur.core.exception.ReservationAlreadyExistsException;
import re1kur.core.exception.ReservationNotFoundException;
import re1kur.core.exception.UserDoesNotHavePermissionForEndpoint;
import re1kur.core.other.JwtExtractor;
import re1kur.core.payload.ReservationPayload;
import re1kur.pars.entity.place.Place;
import re1kur.pars.entity.reservation.Reservation;
import re1kur.pars.mapper.ReservationMapper;
import re1kur.pars.repository.PlaceRepository;
import re1kur.pars.repository.ReservationRepository;
import re1kur.pars.service.parking.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final PlaceRepository placeRepo;
    private final ReservationRepository reservationRepo;
    private final ReservationMapper reservationMapper;

    @Override
    @Transactional
    public ReservationFullDto create(String bearer, ReservationPayload payload) {
        UUID userId = UUID.fromString(JwtExtractor.extractSubFromJwt(bearer));
        Integer number = payload.placeNumber();
        log.info("CREATE RESERVATION [{}] by user [{}]", payload, userId);

        Place parkingPlace = placeRepo.findById(number).orElseThrow(() ->
                new PlaceNotFoundException("Parking place [%d] was not found.".formatted(number)));
        LocalDateTime startAt = payload.startAt();
        LocalDateTime endAt = payload.endAt();
        if (reservationRepo.existsByStartAndEndTime(startAt, endAt))
            throw new ReservationAlreadyExistsException(
                    "Reservation with start time [%s] and end time [%s] already exists."
                    .formatted(startAt.toString(),
                    endAt.toString()));


        Reservation mapped = reservationMapper.create(payload, userId, parkingPlace);
        Reservation saved = reservationRepo.save(mapped);

        log.info("CREATED RESERVATION [{}]", saved.getId());
        return reservationMapper.readFull(saved);
    }

    @Override
    public PageDto<ReservationDto> getPageByUserId(String bearer, Pageable pageable, LocalDate date) {
        UUID userId = UUID.fromString(JwtExtractor.extractSubFromJwt(bearer));

        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            Page<Reservation> found = reservationRepo
                    .findAllByUserIdAndDate(userId, startOfDay, endOfDay, pageable);

            return reservationMapper.pageRead(found);
        }

        Page<Reservation> found = reservationRepo
                .findAllByUserId(userId, pageable);

        return reservationMapper.pageRead(found);
    }

    @Override
    public ReservationDto getById(UUID id, String bearer) {
        UUID userId = UUID.fromString(JwtExtractor.extractSubFromJwt(bearer));

        Reservation found = reservationRepo.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation [%s] was not found.".formatted(userId)));

        return reservationMapper.read(found);
    }

    @Override
    public ReservationFullDto getFullById(UUID id, String bearer) {
        UUID userId = UUID.fromString(JwtExtractor.extractSubFromJwt(bearer));

        Reservation found = reservationRepo.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation [%s] was not found.".formatted(userId)));

        return reservationMapper.readFull(found);
    }

    @Override
    @Transactional
    public void deleteById(UUID id, String bearer) {
        UUID userId = UUID.fromString(JwtExtractor.extractSubFromJwt(bearer));
        log.info("DELETE RESERVATION [{}] by user [{}]", id, userId);

        Reservation found = reservationRepo.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation [%s] was not found.".formatted(userId)));

        UUID owner = found.getUserId();
        if (!Objects.equals(owner, userId))
            throw new UserDoesNotHavePermissionForEndpoint(("User [%s] cannot perform actions " +
                    "with reservation [%s], that does not belong to him.")
                    .formatted(userId, id));

        found.getInformation().setReservation(null);
        found.setInformation(null);
        reservationRepo.delete(found);

        log.info("DELETED RESERVATION [{}] by user [{}]", id, userId);
    }
}

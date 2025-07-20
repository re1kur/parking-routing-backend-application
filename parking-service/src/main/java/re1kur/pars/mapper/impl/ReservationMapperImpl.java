package re1kur.pars.mapper.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import re1kur.core.dto.PageDto;
import re1kur.core.dto.ReservationDto;
import re1kur.core.dto.ReservationFullDto;
import re1kur.core.payload.ReservationPayload;
import re1kur.pars.entity.place.Place;
import re1kur.pars.entity.reservation.Reservation;
import re1kur.pars.entity.reservation.ReservationInformation;
import re1kur.pars.mapper.ReservationMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ReservationMapperImpl implements ReservationMapper {
    @Override
    public Reservation create(ReservationPayload payload, UUID userId, Place parkingPlace) {
        Reservation build = Reservation.builder()
                .place(parkingPlace)
                .carId(payload.carId())
                .userId(userId)
                .build();
        ReservationInformation information = ReservationInformation.builder()
                .reservation(build)
                .reservedAt(LocalDateTime.now())
                .startAt(payload.startAt())
                .endAt(payload.endAt())
                .build();
        build.setInformation(information);

        return build;
    }

    @Override
    public ReservationDto read(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .place(reservation.getPlace().getNumber())
                .carId(reservation.getCarId())
                .userId(reservation.getUserId())
                .paid(reservation.getPaid())
                .build();
    }

    @Override
    public ReservationFullDto readFull(Reservation reservation) {
        ReservationInformation information = reservation.getInformation();
        return ReservationFullDto.builder()
                .id(reservation.getId())
                .place(reservation.getPlace().getNumber())
                .userId(reservation.getUserId())
                .carId(reservation.getCarId())
                .reservedAt(information.getReservedAt())
                .startAt(information.getStartAt())
                .endAt(information.getEndAt())
                .paid(reservation.getPaid())
                .build();
    }

    @Override
    public PageDto<ReservationDto> pageRead(Page<Reservation> found) {
        return new PageDto<>(
                found.getNumber(),
                found.getTotalPages(),
                found.getSize(),
                found.getContent().stream().map(this::read).toList());
    }
}

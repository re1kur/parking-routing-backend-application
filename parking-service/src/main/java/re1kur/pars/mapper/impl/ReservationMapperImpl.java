package re1kur.pars.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.payload.ParkingPlaceReservationPayload;
import re1kur.pars.entity.ParkingPlace;
import re1kur.pars.entity.ParkingPlaceReservation;
import re1kur.pars.mapper.ReservationMapper;

import java.util.UUID;

@Component
public class ReservationMapperImpl implements ReservationMapper {
    @Override
    public ParkingPlaceReservation create(ParkingPlaceReservationPayload payload, UUID userId, ParkingPlace parkingPlace) {
        return ParkingPlaceReservation.builder()
                .parkingPlace(parkingPlace)
                .occupantUserId(userId)
                .reservedAt(payload.reservedAt())
                .endsAt(payload.endsAt())
                .build();
    }

    @Override
    public ParkingPlaceReservationDto read(ParkingPlaceReservation reservation) {
        return ParkingPlaceReservationDto.builder()
                .id(reservation.getId())
                .occupantUserId(reservation.getOccupantUserId())
                .reservedAt(reservation.getReservedAt())
                .endsAt(reservation.getEndsAt())
                .isPaid(reservation.getIsPaid())
                .build();
    }
}

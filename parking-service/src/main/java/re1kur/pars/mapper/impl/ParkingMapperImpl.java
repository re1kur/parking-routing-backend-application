package re1kur.pars.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.dto.ParkingPlaceShortDto;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.entity.Car;
import re1kur.pars.entity.ParkingPlace;
import re1kur.pars.entity.ParkingPlaceInformation;
import re1kur.pars.entity.ParkingPlaceReservation;
import re1kur.pars.mapper.ParkingMapper;

@Component
public class ParkingMapperImpl implements ParkingMapper {
    @Override
    public ParkingPlace create(ParkingPlacePayload payload) {
        return ParkingPlace.builder()
                .number(payload.number())
                .latitude(payload.latitude())
                .longitude(payload.longitude())
                .build();
    }

    @Override
    public ParkingPlaceShortDto readShort(ParkingPlace parkingPlace) {
        return ParkingPlaceShortDto.builder()
                .number(parkingPlace.getNumber())
                .latitude(parkingPlace.getLatitude())
                .longitude(parkingPlace.getLongitude())
                .build();
    }

    @Override
    public ParkingPlaceDto read(ParkingPlace parkingPlace) {
        ParkingPlaceInformation information = parkingPlace.getInformation();
        Car occupantCar = information.getOccupantCar();
        ParkingPlaceReservation reservation = parkingPlace.getReservation();
        return ParkingPlaceDto.builder()
                .number(parkingPlace.getNumber())
                .latitude(parkingPlace.getLatitude())
                .longitude(parkingPlace.getLongitude())
                .isAvailable(information.getIsAvailable())
                .occupantCarId(occupantCar == null ? null : occupantCar.getId())
                .reservationId(reservation == null ? null : reservation.getId())
                .build();
    }

    @Override
    public ParkingPlaceFullDto readFull(ParkingPlace parkingPlace) {
        ParkingPlaceInformation information = parkingPlace.getInformation();
        ParkingPlaceReservation reservation = parkingPlace.getReservation();
        Car occupantCar = information.getOccupantCar();
        ParkingPlaceReservationDto reservationDto = null;
        if (reservation != null) {
            reservationDto = ParkingPlaceReservationDto.builder()
                    .id(reservation.getId())
                    .reservedAt(reservation.getReservedAt())
                    .endsAt(reservation.getEndsAt())
                    .isPaid(reservation.getIsPaid())
                    .build();
        }
        return ParkingPlaceFullDto.builder()
                .number(parkingPlace.getNumber())
                .latitude(parkingPlace.getLatitude())
                .longitude(parkingPlace.getLongitude())
                .isAvailable(information.getIsAvailable())
                .occupantCarId(occupantCar == null ? null : occupantCar.getId())
                .reservation(reservationDto)
                .build();
    }
}

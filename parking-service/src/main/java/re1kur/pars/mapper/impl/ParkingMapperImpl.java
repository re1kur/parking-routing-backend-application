package re1kur.pars.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import re1kur.core.dto.ParkingPlaceDto;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.dto.ParkingPlaceShortDto;
import re1kur.core.payload.ParkingPlacePayload;
import re1kur.pars.entity.car.Car;
import re1kur.pars.entity.place.ParkingPlace;
import re1kur.pars.entity.place.ParkingPlaceInformation;
import re1kur.pars.entity.place.Reservation;
import re1kur.pars.mapper.ParkingMapper;
import re1kur.pars.mapper.ReservationMapper;

@Component
@RequiredArgsConstructor
public class ParkingMapperImpl implements ParkingMapper {
    private final ReservationMapper reservationMapper;

    @Override
    public ParkingPlace create(ParkingPlacePayload payload) {
        ParkingPlace build = ParkingPlace.builder()
                .number(payload.number())
                .latitude(payload.latitude())
                .longitude(payload.longitude())
                .build();
        ParkingPlaceInformation buildInfo = ParkingPlaceInformation.builder()
                .parkingPlace(build).build();
        build.setInformation(buildInfo);
        return build;
//        return ParkingPlace.builder()
//                .number(payload.number())
//                .latitude(payload.latitude())
//                .longitude(payload.longitude())
//                .build();
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
        Reservation reservation = parkingPlace.getReservation();
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
        Reservation reservation = parkingPlace.getReservation();
        Car occupantCar = information.getOccupantCar();
        ParkingPlaceReservationDto mappedReservation = null;
        if (reservation != null) {
            mappedReservation = reservationMapper.read(reservation);
        }
        return ParkingPlaceFullDto.builder()
                .number(parkingPlace.getNumber())
                .latitude(parkingPlace.getLatitude())
                .longitude(parkingPlace.getLongitude())
                .isAvailable(information.getIsAvailable())
                .occupantCarId(occupantCar == null ? null : occupantCar.getId())
                .reservation(mappedReservation)
                .build();
    }

    @Override
    public ParkingPlace clear(ParkingPlace parkingPlace) {
        parkingPlace.setReservation(null);
        ParkingPlaceInformation information = parkingPlace.getInformation();
        information.setOccupantCar(null);
        information.setIsAvailable(true);
        return parkingPlace;
    }
}

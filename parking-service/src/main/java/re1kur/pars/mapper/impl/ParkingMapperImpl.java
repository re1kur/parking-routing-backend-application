package re1kur.pars.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.ParkingPlaceFullDto;
import re1kur.core.dto.PlaceDto;
import re1kur.core.payload.PlacePayload;
import re1kur.pars.entity.place.Place;
import re1kur.pars.mapper.ParkingMapper;

@Component
public class ParkingMapperImpl implements ParkingMapper {

    @Override
    public Place create(PlacePayload payload) {
        return Place.builder()
                .number(payload.number())
                .latitude(payload.latitude())
                .longitude(payload.longitude())
                .build();
    }

    @Override
    public PlaceDto read(Place parkingPlace) {
        return PlaceDto.builder()
                .number(parkingPlace.getNumber())
                .latitude(parkingPlace.getLatitude())
                .longitude(parkingPlace.getLongitude())
                .build();
    }

    @Override
    public ParkingPlaceFullDto readFull(Place parkingPlace) {
//        ParkingPlaceReservationDto mappedReservation = null;
//        if (reservation != null) {
//            mappedReservation = reservationMapper.read(reservation);
//        }
//        return ParkingPlaceFullDto.builder()
//                .number(parkingPlace.getNumber())
//                .latitude(parkingPlace.getLatitude())
//                .longitude(parkingPlace.getLongitude())
//                .isAvailable(information.getIsAvailable())
//                .occupantCarId(occupantCar == null ? null : occupantCar.getId())
//                .reservation(mappedReservation)
//                .build();
        return null;
    }

    @Override
    public Place update(Place found, PlacePayload payload) {
        found.setLatitude(payload.latitude());
        found.setLongitude(payload.longitude());
        return found;
    }
}

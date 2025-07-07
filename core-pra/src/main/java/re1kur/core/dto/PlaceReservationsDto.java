package re1kur.core.dto;

import java.util.List;

public record PlaceReservationsDto(
        Integer placeNumber,
        List<ParkingPlaceReservationDto> reservations) {
}

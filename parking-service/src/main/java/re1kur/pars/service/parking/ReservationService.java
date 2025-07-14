package re1kur.pars.service.parking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.core.dto.PlaceReservationsDto;
import re1kur.core.payload.ParkingPlaceReservationPayload;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReservationService {
    ParkingPlaceReservationDto create(String token, ParkingPlaceReservationPayload payload);

    List<PlaceReservationsDto> getListForToday();

    List<ParkingPlaceReservationDto> getListByPlaceNumberForToday(Integer number);

    List<PlaceReservationsDto> getListForDate(OffsetDateTime date);

    Page<ParkingPlaceReservationDto> getPageByUserId(String token, Pageable pageable);

    Page<ParkingPlaceReservationDto> getPageByUserIdAndDate(String token, OffsetDateTime offsetDateTime, Pageable pageable);
}

package re1kur.pars.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import re1kur.core.dto.ParkingPlaceReservationDto;
import re1kur.pars.entity.ParkingPlaceReservation;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends CrudRepository<ParkingPlaceReservation, UUID> {
    @Query(nativeQuery = true,
            value = """
                        SELECT * FROM parking_place_reservations 
                        WHERE place_number = :placeNumber 
                          AND reserved_at >= date_trunc('day', now())
                    """)
    List<ParkingPlaceReservation> findAllByNumberToday(@Param("placeNumber") Integer placeNumber);


    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM parking_place_reservations 
                    WHERE place_number = :placeNumber
                    AND reserved_at >= :date
                    """)
    List<ParkingPlaceReservation> findAllByNumberForDate(
            @Param("placeNumber") Integer number,
            @Param("date") OffsetDateTime date);

    Page<ParkingPlaceReservation> findAllByOccupantUserId(UUID userId, Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM parking_place_reservations 
                    WHERE occupant_user_id = :userId
                    AND reserved_at >= :date
                    """)
    Page<ParkingPlaceReservation> findAllByOccupantUserIdAndDate(
            @Param("userId") UUID userId,
            Pageable pageable,
            @Param("date") OffsetDateTime date);
}


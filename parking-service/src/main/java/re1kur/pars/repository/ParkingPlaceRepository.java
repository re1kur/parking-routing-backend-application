package re1kur.pars.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import re1kur.pars.entity.place.Place;

import java.time.LocalDate;
import java.util.List;

public interface ParkingPlaceRepository extends CrudRepository<Place, Integer> {
    @Query(nativeQuery = true, value =
            """
                    SELECT p.number FROM parking_places p
                    """)
    List<Integer> findAllNumbers();

    Page<Place> findAll(Pageable pageable);

    boolean existsByLatitudeAndLongitude(Float payloadLatitude, Float payloadLongitude);

    @Query(nativeQuery = true, value =
//            """
//            SELECT p.number FROM parking_places p
//            WHERE p.number NOT IN (
//            SELECT r.place_number
//             FROM reservations r
//            JOIN reservation_information ri ON ri.reservation_id=r.id
//            WHERE
//             paid IS TRUE
//             AND ? >= start_at
//             AND ? <= end_at
//            );
//            """
            """
            SELECT p.number
            FROM parking_places p
            WHERE NOT EXISTS (
                SELECT 1
                FROM reservations r
                JOIN reservation_information ri ON ri.reservation_id = r.id
                WHERE r.place_number = p.number
                  AND paid IS TRUE
                  AND ? >= start_at
                  AND ? <= end_at
            );
            """
    )
    List<Place> findAllAvailableNumbersByDate(LocalDate today);
}

package re1kur.pars.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import re1kur.pars.entity.place.Place;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PlaceRepository extends CrudRepository<Place, Integer> {
    Page<Place> findAll(Pageable pageable);

    boolean existsByLatitudeAndLongitude(BigDecimal payloadLatitude, BigDecimal payloadLongitude);

    @Query(nativeQuery = true, value =
            """
                    SELECT p.number
                    FROM parking_places p
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM reservations r
                        JOIN reservation_information ri ON ri.reservation_id = r.id
                        WHERE r.place_number = p.number
                          AND paid IS TRUE
                          AND :start_day >= start_at
                          AND :end_day <= end_at
                    );
                    """
    )
    List<Integer> findAvailableNumbersByDate(@Param("start_day") LocalDateTime startOfDay,
                                             @Param("end_day") LocalDateTime endOfDay);

    @Query(nativeQuery = true,
            value = """
                    SELECT p.number
                    FROM parking_places p
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM reservations r
                        JOIN reservation_information ri ON ri.reservation_id = r.id
                        WHERE r.place_number = p.number
                        AND :start_at >= start_at
                        AND :end_at <= end_at
                    )
                    """)
    List<Integer> findAvailableNumbersByStartAndEnd(
            @Param("start_at") LocalDateTime startAt,
            @Param("end_at") LocalDateTime endAt);
}

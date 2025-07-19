package re1kur.pars.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import re1kur.pars.entity.reservation.Reservation;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ReservationRepository extends CrudRepository<Reservation, UUID> {

    @Query("""
                SELECT r
                FROM Reservation r
                WHERE r.userId = :userId
                AND r.information.startAt <= :endOfDay
                AND r.information.endAt >= :startOfDay
            """)
    Page<Reservation> findAllByUserIdAndDate(
            @Param("userId") UUID userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            Pageable pageable
    );

    @Query("""
                SELECT r
                FROM Reservation r
                WHERE r.userId = :userId
            """)
    Page<Reservation> findAllByUserId(
            @Param("userId") UUID userId,
            Pageable pageable
    );

    @Query(nativeQuery = true,
            value = """
                    SELECT EXISTS (
                        SELECT 1 FROM reservations r
                        JOIN reservation_information ri ON ri.reservation_id = r.id
                            WHERE ri.start_at = :startAt
                            AND ri.end_at = :endAt
                    );
                    """)
    boolean existsByStartAndEndTime(
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt
    );
}
package re1kur.pars.entity.reservation;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservation_information")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "reservation")
public class ReservationInformation {

    @Id
    private UUID reservationId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(insertable = false, columnDefinition = "DEFAULT now()")
    private LocalDateTime reservedAt;

    private LocalDateTime endAt;

    private LocalDateTime startAt;
}

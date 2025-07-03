package re1kur.pars.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "parking_place_reservations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "parkingPlace")
public class ParkingPlaceReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "reservation")
    @JoinColumn(name = "place_number")
    private ParkingPlace parkingPlace;

    private UUID occupantUserId;

    private OffsetDateTime reservedAt;

    private OffsetDateTime endsAt;

    private Boolean isPaid;
}

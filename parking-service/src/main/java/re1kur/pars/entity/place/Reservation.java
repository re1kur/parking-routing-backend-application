package re1kur.pars.entity.place;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "parkingPlace")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "reservation")
    @JoinColumn(name = "place_number")
    private ParkingPlace parkingPlace;

    private UUID occupantUserId;

    private OffsetDateTime reservedAt;

    private OffsetDateTime endsAt;

    @Column(insertable = false, columnDefinition = "DEFAULT FALSE")
    private Boolean isPaid;
}

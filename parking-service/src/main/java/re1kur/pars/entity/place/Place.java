package re1kur.pars.entity.place;

import jakarta.persistence.*;
import lombok.*;
import re1kur.pars.entity.reservation.Reservation;

import java.math.BigDecimal;
import java.util.List;

@Entity
    @Table(name = "parking_places")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "reservations")
public class Place {
    @Id
    private Integer number;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "number")
    private List<Reservation> reservations;
}

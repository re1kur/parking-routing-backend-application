package re1kur.pars.entity.place;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_places")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingPlace {
    @Id
    private Integer number;

    private Float latitude;

    private Float longitude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "number")
    private ParkingPlaceInformation information;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "number")
    private Reservation reservation;
}

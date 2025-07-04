package re1kur.pars.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parking_place_information")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "parkingPlace")
public class ParkingPlaceInformation {

    @Id
    private Integer parkingPlaceNumber;

    @MapsId
    @OneToOne(mappedBy = "information")
    private ParkingPlace parkingPlace;

    @OneToOne
    @JoinColumn(name = "occupant_car_id")
    private Car OccupantCar;

    @Column(insertable = false, columnDefinition = "DEFAULT FALSE")
    private Boolean isAvailable;
}
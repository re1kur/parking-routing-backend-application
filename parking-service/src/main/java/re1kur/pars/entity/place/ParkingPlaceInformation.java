package re1kur.pars.entity.place;

import jakarta.persistence.*;
import lombok.*;
import re1kur.pars.entity.car.Car;

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
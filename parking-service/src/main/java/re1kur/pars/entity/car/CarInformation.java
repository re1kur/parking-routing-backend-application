package re1kur.pars.entity.car;

import jakarta.persistence.*;
import lombok.*;
import re1kur.pars.entity.Make;

import java.util.UUID;

@Entity
@Table(name = "car_information")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "car")
public class CarInformation {
    @Id
    private UUID carId;

    @MapsId
    @OneToOne(mappedBy = "carInformation")
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "make_id")
    private Make make;

    private String color;

    private String model;
}

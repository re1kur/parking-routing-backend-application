package re1kur.pars.entity.region;

import jakarta.persistence.*;
import lombok.*;
import re1kur.pars.entity.car.Car;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "region_codes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "region")
public class RegionCode {
    @Id
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(mappedBy = "regionCode")
    private Collection<Car> cars = new ArrayList<>();
}

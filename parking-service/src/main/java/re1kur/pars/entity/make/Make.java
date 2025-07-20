package re1kur.pars.entity.make;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re1kur.pars.entity.car.CarInformation;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "makes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Make {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "make", fetch = FetchType.LAZY)
    private Collection<CarInformation> cars = new ArrayList<>();
}

package re1kur.pars.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "region_codes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Code {
    @Id
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    private Region region;

    @OneToMany(mappedBy = "regionCode")
    private Collection<Car> cars = new ArrayList<>();
}

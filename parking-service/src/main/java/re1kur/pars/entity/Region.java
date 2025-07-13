package re1kur.pars.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "regions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "region_codes",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "region_id"))
    private Collection<RegionCode> regionCodes = new ArrayList<>();
}

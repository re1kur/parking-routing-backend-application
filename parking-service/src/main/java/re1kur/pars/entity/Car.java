package re1kur.pars.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cars")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID ownerId;

    private String licensePlate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_code")
    private RegionCode regionCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private CarInformation carInformation;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "car_images",
    joinColumns = @JoinColumn(name = "id"),
    inverseJoinColumns = @JoinColumn(name = "car_id"))
    private Set<CarImage> images = new HashSet<>();

    public boolean isOwner(UUID userId) {
        return Objects.equals(ownerId, userId);
    }
}
